package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void update(int userId, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    public Meal get(int userId, int id) {
        Meal meal = repository.get(userId, id);
        return checkNotFoundWithId(meal, id);
    }

    public void delete(int userId, int id) {
        Meal meal = repository.get(userId, id);
        checkNotFoundWithId(meal, id);
        repository.delete(userId, id);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getAllFilteredByDate(int userId, LocalDate from, LocalDate to) {
        return repository.getAllFilteredByDate(userId, from, to);
    }
}