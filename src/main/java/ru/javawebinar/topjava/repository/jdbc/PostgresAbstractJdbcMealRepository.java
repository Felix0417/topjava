package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile(Profiles.POSTGRES_DB)
public class PostgresAbstractJdbcMealRepository extends AbstractJdbcMealRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresAbstractJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate1) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.jdbcTemplate = jdbcTemplate1;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meal WHERE user_id=?  AND date_time >=  ? AND date_time < ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, startDateTime, endDateTime);
    }
}
