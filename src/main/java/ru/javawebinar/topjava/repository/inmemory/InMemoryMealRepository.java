package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(1, meal));
        MealsUtil.meals.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        Map<Integer, Meal> tempMap = repository.getOrDefault(userId, new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            tempMap.putIfAbsent(meal.getId(), meal);
            repository.putIfAbsent(meal.getUserId(), tempMap);
            return meal;
        }
        // handle case: update, but not present in storage
        if (meal.getId() == null) {
            return null;
        }
        return tempMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Integer userId, int id) {
        return checkNotFoundWithId(repository.get(userId).remove(id), id) != null;
    }

    @Override
    public Meal get(Integer userId, int id) {
        return checkNotFoundWithId(repository.get(userId).get(id), id);
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return checkNotFoundWithId(repository.get(userId).values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()), userId);
    }

    @Override
    public List<Meal> getAllFilteredByDate(Integer userId, LocalDate from, LocalDate to) {
        return checkNotFoundWithId(
                repository.get(userId)
                        .values()
                        .stream()
                        .filter(meal ->
                                DateTimeUtil.isBetweenHalfOpen(meal.getDate(), from, to))
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList()), userId);
    }
}