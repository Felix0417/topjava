package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Transactional
    @Override
    public User save(@NotNull @Valid User user) {
        ValidationUtil.validate(validator, user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        boolean isNew = user.isNew();
        if (isNew) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        updateRoles(user.id(), user.getRoles(), isNew);
        return user;
    }

    private void updateRoles(int id, Set<Role> roles, boolean isNew) {
        if (!isNew) {
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", id);
        }

        if (!roles.isEmpty()) {
            String createSql = "INSERT INTO user_role ( role, user_id) VALUES (?,?)";
            Role[] rolesArray = roles.toArray(new Role[0]);
            this.jdbcTemplate.batchUpdate(
                    createSql, new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, rolesArray[i].name());
                            ps.setLong(2, id);
                        }

                        @Override
                        public int getBatchSize() {
                            return roles.size();
                        }
                    });
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        String sql = "SELECT * FROM users LEFT JOIN user_role ur ON users.id = ur.user_id WHERE id=?";
        List<User> users = jdbcTemplate.query(sql, getExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        String sql = "SELECT * FROM users LEFT JOIN user_role ur ON users.id = ur.user_id WHERE email=?";
        List<User> users = jdbcTemplate.query(sql, getExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users LEFT JOIN user_role ur on users.id = ur.user_id  ORDER BY name, email";
        return jdbcTemplate.query(sql, getExtractor());
    }

    //    https://coderlessons.com/tutorials/java-tekhnologii/uznai-vesnu-jdbc/spring-jdbc-interfeis-resultsetextractor
    private ResultSetExtractor<List<User>> getExtractor() {
        return resultSet -> {
            Map<Integer, User> userMap = new LinkedHashMap<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                User user = userMap.computeIfAbsent(id, k -> {
                    try {
                        User newUser = ROW_MAPPER.mapRow(resultSet, 0);
                        newUser.setRoles(EnumSet.noneOf(Role.class));
                        return newUser;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                String role = resultSet.getString("role");
                if (role != null) {
                    user.getRoles().add(Role.valueOf(role));
                }
            }
            return new ArrayList<>(userMap.values());
        };
    }
}