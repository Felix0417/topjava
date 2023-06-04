package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.data.MealsRepository;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private final MealsRepository mealsRepository = new MealsRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealsRepository.getMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59), MealsRepository.CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealToList);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
