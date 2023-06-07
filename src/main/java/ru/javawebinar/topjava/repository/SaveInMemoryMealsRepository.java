package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SaveInMemoryMealsRepository implements MealsRepository {
    private final AtomicInteger id = new AtomicInteger(0);
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    public SaveInMemoryMealsRepository() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        for (Meal meal : meals) {
            save(meal);
        }
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(id.getAndIncrement());
            return mealMap.computeIfAbsent(meal.getId(), newMeal -> meal);
        }
        return mealMap.computeIfPresent(meal.getId(), (k, v) -> meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getById(int id) {
        return mealMap.get(id);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }
}