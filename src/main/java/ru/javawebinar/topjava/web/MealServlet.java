package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealsRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private final MealsRepositoryImpl mealsRepositoryImpl = new MealsRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String forward;
        switch (action == null ? "default" : action) {
            case ("listMeals"):
                log.debug("redirecting to meals");
                forward = "/meals.jsp";
                request.setAttribute("mealsTo", mealsRepositoryImpl.getAllMealsTo());
                break;
            case ("delete"):
                log.debug("deleted meal - {}", Integer.parseInt(request.getParameter("mealId")));
                mealsRepositoryImpl.deleteMeal(Integer.parseInt(request.getParameter("mealId")));
                forward = "/meals.jsp";
                request.setAttribute("mealsTo", mealsRepositoryImpl.getAllMealsTo());
                break;
            case ("edit"):
                log.debug("Editing meal - {}", Integer.parseInt(request.getParameter("mealId")));
                forward = "/edit.jsp";
                Meal meal = mealsRepositoryImpl.getMealById(Integer.parseInt(request.getParameter("mealId")));
                request.setAttribute("meal", meal);
                break;
            case ("create"):
                log.debug("Adding new meal");
                forward = "/create.jsp";
                break;
            default:
                forward = "/meals.jsp";
                request.setAttribute("mealsTo", mealsRepositoryImpl.getAllMealsTo());
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
        if (request.getParameter("id") != null) {
            mealsRepositoryImpl.update(Integer.parseInt(request.getParameter("id")), localDateTime, description, calories);
        } else {
            mealsRepositoryImpl.create(localDateTime, description, calories);
        }
        log.debug("redirecting to meals");
        request.setAttribute("mealsTo", mealsRepositoryImpl.getAllMealsTo());
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
