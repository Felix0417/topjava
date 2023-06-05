package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsRepositoryImpl implements MealsRepository {
    public static final int CALORIES_PER_DAY = 2000;
    private static final AtomicInteger id = new AtomicInteger(0);

    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    public MealsRepositoryImpl() {
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    }

    @Override
    public void create(LocalDateTime localDateTime, String description, int calories) {
        Meal meal = new Meal(id.getAndIncrement(), localDateTime, description, calories);
        mealMap.putIfAbsent(meal.getId(), meal);
    }

    @Override
    public void update(int id, LocalDateTime localDateTime, String description, int calories) {
        mealMap.put(id, new Meal(id, localDateTime, description, calories));
    }

    @Override
    public List<MealTo> getAllMealsTo() {
        return MealsUtil.filteredByStreams(
                new ArrayList<>(mealMap.values()),
                LocalTime.ofNanoOfDay(LocalTime.MIN.toNanoOfDay()),
                LocalTime.ofNanoOfDay(LocalTime.MAX.toNanoOfDay()),
                MealsRepositoryImpl.CALORIES_PER_DAY);
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getMealById(int id) {
        return mealMap.get(id);
    }

    @Override
    public void deleteMeal(int id) {
        mealMap.remove(id);
    }
}
