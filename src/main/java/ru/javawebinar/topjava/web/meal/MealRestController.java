package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private final MealService service;


    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getFiltered(LocalDate from, LocalDate to, LocalTime start, LocalTime end) {
        if (from == null) {
            from = LocalDate.of(1970, 1, 1);
        }
        if (to == null) {
            to = LocalDate.now();
        }
        if (start == null) {
            start = LocalTime.MIN;
        }
        if (end == null) {
            end = LocalTime.MAX;
        }
        return MealsUtil.getFilteredTos(service.getAllFilteredByDate(SecurityUtil.authUserId(), from, to),
                MealsUtil.DEFAULT_CALORIES_PER_DAY, start, end);
    }

    public Meal get(int id) {
        return service.get(SecurityUtil.authUserId(), id);
    }

    public void delete(int id) {
        service.delete(SecurityUtil.authUserId(), id);
    }

    public Meal create(Meal meal) {
        return service.create(SecurityUtil.authUserId(), meal);
    }

    public void update(Meal meal, int id) {
        service.update(SecurityUtil.authUserId(), id, meal);
    }
}