package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.mealsOfFirstUser.forEach(meal -> save(1, meal));
        MealsUtil.mealsOfSecondUser.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap.containsKey(id)) {
            mealMap.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null ? mealMap.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFilteredByDate(userId, null, null);
    }

    @Override
    public List<Meal> getAllFilteredByDate(int userId, LocalDate from, LocalDate to) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        LocalDate toPlusDay = (to != null ? (to.equals(LocalDate.MAX) ? to : to.plusDays(1)) : null);
        return mealMap != null
                ? mealMap.values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), from, toPlusDay))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList())
                : Collections.emptyList();
    }
}