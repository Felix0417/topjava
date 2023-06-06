package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealsRepository;
import ru.javawebinar.topjava.repository.MealsRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealsRepository mealsRepository;
    private User user;

    @Override
    public void init() throws ServletException {
        super.init();
        mealsRepository = new MealsRepositoryImpl();
        user = new User();
        for (Meal meal : user.getMeals()) {
            mealsRepository.createOrUpdate(meal.getId(), meal);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String mealId = request.getParameter("mealId");
        String forward;
        switch (action == null ? "default" : action) {
            case ("delete"):
                log.debug("deleted meal - {}", mealId);
                mealsRepository.delete(Integer.parseInt(mealId));
                forward = "/meals.jsp";
                request.setAttribute("mealsTo", getMealToList(mealsRepository.getAll(), user.CALORIES_PER_DAY));
                break;
            case ("edit"):
                log.debug("Editing meal - {}", mealId);
                forward = "/createOrEdit.jsp";
                Meal meal = mealsRepository.getById(Integer.parseInt(mealId));
                request.setAttribute("meal", meal);
                break;
            case ("create"):
                log.debug("Adding new meal");
                forward = "/createOrEdit.jsp";
                break;
            default:
                forward = "/meals.jsp";
                request.setAttribute("mealsTo", getMealToList(mealsRepository.getAll(), user.CALORIES_PER_DAY));
                break;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(localDateTime, description, calories);
        String id = request.getParameter("id");
        meal.setId(id.equals("-1") ? null : Integer.parseInt(id));
        meal = mealsRepository.createOrUpdate(meal.getId(), meal);
        log.debug("Данные успешно записаны");
        log.debug("redirecting to meals");
        response.sendRedirect(request.getContextPath() + "/meals");
    }

    public static List<MealTo> getMealToList(List<Meal> mealList, int caloriesPerDay) {
        return MealsUtil.filteredByStreams(
                new ArrayList<>(mealList),
                LocalTime.MIN,
                LocalTime.MAX,
                caloriesPerDay);
    }
}