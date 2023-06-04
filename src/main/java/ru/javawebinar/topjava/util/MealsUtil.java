package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.data.MealsRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsUtil {
    private static final MealsRepository mealsRepository = new MealsRepository();

    public static void main(String[] args) {
        List<Meal> meals = mealsRepository.getMeals();
        int caloriesPerDay = MealsRepository.CALORIES_PER_DAY;

        List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), caloriesPerDay);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
