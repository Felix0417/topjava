package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Meal m SET m.dateTime = :datetime, m.calories= :calories," +
            " m.description=:desc where m.id=:id and m.user.id=:userId")
    int save(@Param("id") int id,
             @Param("datetime") LocalDateTime dateTime,
             @Param("desc") String description,
             @Param("calories") int calories,
             @Param("userId") Integer userId);

    @Query("select m from Meal m where m.id=:id AND m.user.id=:userId")
    Optional<Meal> findById(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Modifying
    @Query("DELETE Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m where m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> findAllById(@Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.dateTime >= :startDateTime AND m.dateTime < :endDateTime " +
            "ORDER BY m.dateTime DESC")
    List<Meal> findAllBetween(@Param("startDateTime") LocalDateTime startDateTime,
                              @Param("endDateTime") LocalDateTime endDateTime,
                              @Param("userId") int userId);

//    https://stackoverflow.com/questions/5903774/ordering-a-join-fetched-collection-in-jpa-using-jpql-hql
    @Query("SELECT m from Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId")
    Optional<Meal> getWithUser(@Param("id") int id, @Param("userId") int userId);
}
