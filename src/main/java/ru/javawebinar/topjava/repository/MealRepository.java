package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {
    Meal save(Integer userId, Meal meal);

    boolean delete(Integer userId, int id);

    Meal get(Integer userId, int id);

    List<Meal> getAll(Integer userId);

    List<Meal> getAllFilteredByDate(Integer userId, LocalDate from, LocalDate to);
}
