package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {
    private final MealService service;

    private final Logger log = LoggerFactory.getLogger(MealRestController.class);

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.debug("get All");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate from, LocalDate to, LocalTime start, LocalTime end) {
        log.debug("get filtered");
        if (from == null) {
            from = LocalDate.MIN;
        }
        if (to == null) {
            to = LocalDate.MAX;
        }
        if (start == null) {
            start = LocalTime.MIN;
        }
        if (end == null) {
            end = LocalTime.MAX;
        }
        return MealsUtil.getFilteredTos(service.getAllFilteredByDate(SecurityUtil.authUserId(), from, to),
                SecurityUtil.authUserCaloriesPerDay(), start, end);
    }

    public Meal get(int id) {
        log.debug("get {}", id);
        return service.get(SecurityUtil.authUserId(), id);
    }

    public void delete(int id) {
        log.debug("delete {}", id);
        service.delete(SecurityUtil.authUserId(), id);
    }

    public Meal create(Meal meal) {
        ValidationUtil.checkNew(meal);
        log.debug("create {}", meal);
        return service.create(SecurityUtil.authUserId(), meal);
    }

    public void update(Meal meal, int id) {
        log.debug("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(SecurityUtil.authUserId(), meal);
    }
}