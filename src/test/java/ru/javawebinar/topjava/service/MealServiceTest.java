package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), UserTestData.USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, UserTestData.USER_ID), newMeal);
    }

    @Test
    public void duplicateMealDate() {
        assertThrows(DataAccessException.class,
                () -> service.create(new Meal(MealTestData.userMeal1.getDateTime(), "Duplicate", 100),
                        UserTestData.USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.USER_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.NOT_FOUND, UserTestData.USER_ID));
    }

    @Test
    public void deletedFromAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.userMeal1.getId(), UserTestData.NOT_FOUND));
    }

    @Test
    public void get() {
        Meal meal = new Meal(MealTestData.userMeal1);
        MealTestData.assertMatch(meal, service.get(meal.getId(), UserTestData.USER_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.NOT_FOUND, UserTestData.USER_ID));
    }

    @Test
    public void getFromAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.userMeal1.getId(), UserTestData.NOT_FOUND));
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, UserTestData.USER_ID);
        MealTestData.assertMatch(service.get(updated.getId(), UserTestData.USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateFromAnotherUser() {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, UserTestData.NOT_FOUND));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(UserTestData.USER_ID);
        MealTestData.assertMatch(all, MealTestData.meals);
    }

    @Test
    public void getBetweenHalfOpen() {
        MealTestData.assertMatch(MealTestData.meals, service.getBetweenInclusive(null, null, UserTestData.USER_ID));
    }

    @Test
    public void getBetweenHalfOpenToFirstUser() {
        LocalDate mealDate = MealTestData.userMeal1.getDateTime().toLocalDate();
        MealTestData.assertMatch(MealTestData.meals,
                service.getBetweenInclusive(mealDate.minusDays(2), mealDate.plusDays(2), UserTestData.USER_ID));
    }
}