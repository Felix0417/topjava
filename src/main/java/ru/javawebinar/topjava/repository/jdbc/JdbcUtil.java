package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public class JdbcUtil {
    public static <T> void validate(Validator validator, T t){
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid arguments to save " , violations);
        }
    }
}
