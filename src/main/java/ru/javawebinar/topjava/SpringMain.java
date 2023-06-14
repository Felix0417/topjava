package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            //TESTING
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.getAll();

            mealRestController.update(new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "TEST", 7777), 5);
            System.out.println(mealRestController.getAll());

            mealRestController.delete(400);
            mealRestController.delete(7);
            System.out.println(mealRestController.getAll());

            mealRestController.getAll();
            System.out.println(mealRestController.getFiltered(
                    LocalDate.of(2020, Month.JANUARY, 30),
                    LocalDate.of(2020, Month.JANUARY, 31),
                    LocalTime.of(7, 0),
                    LocalTime.of(13, 0)));
            System.out.println(mealRestController.getFiltered(
                    null,
                    null,
                    LocalTime.of(7, 0),
                    LocalTime.of(12, 0)));
        }
    }
}