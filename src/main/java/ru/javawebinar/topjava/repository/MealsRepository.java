package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsRepository {
    Meal save(Meal meal);

    List<Meal> getAll();

    Meal getById(int id);

    void delete(int id);
}