package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_MEAL_URL + "/";

    @Autowired
    private MealService mealService;

    @Test
    void getAll() throws Exception {
        List<MealTo> mealToList = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealToList));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> mealService.get(USER_ID, MEAL1_ID));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal meal = MealTestData.getNew();
        ResultActions actions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(meal)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(actions);
        int newId = created.id();
        meal.setId(newId);
        MEAL_MATCHER.assertMatch(created, meal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, SecurityUtil.authUserId()), meal);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + updated.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(updated.id(), SecurityUtil.authUserId()), updated);
    }

    @Test
    void getBetweenLocalDateTime() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.parse("2020-01-31T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-01-31T23:59:59");
        String queryParams = String.format("filter?startDateTime=%s&endDateTime=%s", startDateTime, endDateTime);

        List<MealTo> mealToList = MealsUtil.getTos(filteredMeals, SecurityUtil.authUserCaloriesPerDay());

        perform(MockMvcRequestBuilders.get(REST_URL + queryParams))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(mealToList));
    }
}