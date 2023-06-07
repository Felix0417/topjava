package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealsRepository;
import ru.javawebinar.topjava.repository.MealsRepositorySave;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MealServlet extends HttpServlet {
    static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private final MealsRepository mealsRepository = new MealsRepositorySave();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String mealId = request.getParameter("mealId");
        String forward;
        switch (action == null ? "default" : action) {
            case ("edit"):
                log.debug("Editing meal - {}", mealId);
                forward = "/createOrEdit.jsp";
                Meal meal = mealsRepository.getById(Integer.parseInt(mealId));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            case ("create"):
                log.debug("Adding new meal");
                forward = "/createOrEdit.jsp";
                LocalDateTime localDateTime = LocalDateTime.parse(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
                request.setAttribute("meal", new Meal(localDateTime, " ", 0));
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            case ("delete"):
                log.debug("deleted meal - {}", mealId);
                mealsRepository.delete(Integer.parseInt(mealId));
                request.setAttribute("mealsTo", MealsUtil.getMealToList(mealsRepository.getAll(), CALORIES_PER_DAY));
                response.sendRedirect(request.getContextPath() + "/meals");
                break;
            default:
                forward = "/meals.jsp";
                request.setAttribute("mealsTo", MealsUtil.getMealToList(mealsRepository.getAll(), CALORIES_PER_DAY));
                request.getRequestDispatcher(forward).forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(localDateTime, description, calories);
        String id = request.getParameter("id");
        meal.setId(id.equals("") ? null : Integer.parseInt(id));
        mealsRepository.save(meal);
        log.debug("data saved successfully");
        log.debug("redirecting to meals");
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}