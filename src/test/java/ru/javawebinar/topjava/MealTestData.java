package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ + 3;
    public static final int ADMIN_MEAL_ID = START_SEQ + 10;
    public static final int NOT_FOUND = 100;

    public static final Meal userMeal1 = new Meal(USER_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак USER", 500);
    public static final Meal userMeal2 = new Meal(USER_MEAL_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед USER", 1000);
    public static final Meal userMeal3 = new Meal(USER_MEAL_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин USER", 500);
    public static final Meal userMeal4 = new Meal(USER_MEAL_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение USER", 100);
    public static final Meal userMeal5 = new Meal(USER_MEAL_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак USER", 1000);
    public static final Meal userMeal6 = new Meal(USER_MEAL_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед USER", 500);
    public static final Meal userMeal7 = new Meal(USER_MEAL_ID + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин USER", 410);
    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак ADMIN", 500);
    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед ADMIN", 1000);
    public static final Meal adminMeal3 = new Meal(ADMIN_MEAL_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин ADMIN", 500);

    public static List<Meal> meals = Arrays.asList(userMeal7, userMeal6, userMeal5, userMeal4, userMeal3,
            userMeal2, userMeal1);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(1900, Month.JANUARY, 1, 12, 1), "Новая еда", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal1);
        updated.setDateTime(updated.getDateTime().plusSeconds(1));
        updated.setDescription("Обновленная еда");
        updated.setCalories(5000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("description").isEqualTo(expected);
    }
}