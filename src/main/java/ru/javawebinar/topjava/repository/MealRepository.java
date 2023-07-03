package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    // null if updated meal does not belong to userId
    Meal save(Meal meal, int userId);

    // false if meal does not belong to userId
    boolean delete(int id, int userId);

    // null if meal does not belong to userId
    Meal get(int id, int userId);

    // ORDERED dateTime desc
    List<Meal> getAll(int userId);

    // ORDERED dateTime desc
    List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);

//    https://stackoverflow.com/questions/10572643/optional-methods-in-java-interface/41293086?_x_tr_sl=auto&_x_tr_tl=ru&_x_tr_hl=ru#41293086
    default Meal getWithUser(int id, int userId) {
        throw  new UnsupportedOperationException();
    }
}
