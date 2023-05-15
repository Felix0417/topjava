package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> date = new HashMap<>();
        List<UserMeal> tempData = new ArrayList<>();
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        LocalDate localDate;

        for (int i = 0; i < meals.size(); i++) {
            localDate = meals.get(i).getDateTime().toLocalDate();
            assert false;
            if (isNull((date.get(localDate)))) {
                date.put(localDate, 0);
            }
            int tempCalories = meals.get(i).getCalories();
            date.computeIfPresent(localDate, (key, value) -> value + tempCalories);
            if (TimeUtil.isBetweenHalfOpen(meals.get(i).getDateTime().toLocalTime(), startTime, endTime)) {
                tempData.add(meals.get(i));
            }

            if (i == meals.size() - 1) {
                for (UserMeal userMeal : tempData) {
                    userMealWithExcesses.add(new UserMealWithExcess(
                            userMeal.getDateTime(),
                            userMeal.getDescription(),
                            userMeal.getCalories(),
                            date.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
                }
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> temp = new HashMap<>();

        return meals.stream()
          .peek(x -> {
                LocalDate date = x.getDateTime().toLocalDate();
                if (isNull(temp.get(date))) {
                    temp.put(date, 0);
                }
                temp.computeIfPresent(date, (k, v) -> v + x.getCalories());
                })
          .filter(x -> TimeUtil.isBetweenHalfOpen(x.getDateTime().toLocalTime(), startTime, endTime))
          .sorted(Comparator.comparing(UserMeal::getDateTime)).map(x -> {
                int calories = temp.get(x.getDateTime().toLocalDate());
                return new UserMealWithExcess(x.getDateTime(), x.getDescription(), x.getCalories(), calories > caloriesPerDay);
                })
          .collect(Collectors.toList());
    }
}
