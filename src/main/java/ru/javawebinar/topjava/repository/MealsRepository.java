package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.util.List;

public interface MealsRepository {
    void create(LocalDateTime localDateTime, String description, int calories);

    void update(int id, LocalDateTime localDateTime, String description, int calories);

    List<MealTo> getAllMealsTo();

    Meal getMealById(int id);

    void deleteMeal(int id);
}
