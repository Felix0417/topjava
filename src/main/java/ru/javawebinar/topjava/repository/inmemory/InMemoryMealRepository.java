package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

//     {
//        MealsUtil.meals.forEach(meal -> save(null, meal));
//    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        Map<Integer, Meal> tempMap = repository.getOrDefault(userId, new HashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            tempMap.put(meal.getId(), meal);
            repository.put(userId, tempMap);
            return meal;
        }
        // handle case: update, but not present in storage
        tempMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        repository.computeIfPresent(userId, (id, oldMeal) -> tempMap);
        return meal;
    }

    @Override
    public boolean delete(Integer userId, int id) {
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(Integer userId, int id) {
        return repository.get(userId).getOrDefault(id, null);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository.get(userId).values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllFilteredByDate(Integer userId, LocalDate from, LocalDate to) {
        return getAll(userId).stream().filter(meal ->
                        meal.getDateTime().toLocalDate().isAfter(from.minusDays(1)) &&
                                meal.getDateTime().toLocalDate().isBefore(to.plusDays(1)))
                .collect(Collectors.toList());
    }
}