package com.davidmaisuradze.gymapplication.repository;

import com.davidmaisuradze.gymapplication.entity.Trainee;
import com.davidmaisuradze.gymapplication.entity.Trainer;
import com.davidmaisuradze.gymapplication.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUsernameIgnoreCase(String username);

    @Query("select t from Trainer t where t not in (select tr.trainer from Training tr where lower(tr.trainee.username) = :username) and t.isActive = true")
    List<Trainer> getTrainersNotAssigned(@Param("username") String username);

    @Query("select t.trainee from Training t where lower(t.trainer.username) = :username")
    List<Trainee> getAllTrainees(@Param("username") String username);

    @Query("select t from Training t where lower(t.trainer.username) = :username " +
            "and (:fromDate is null or t.trainingDate >= :fromDate) " +
            "and (:toDate is null or t.trainingDate <= :toDate) " +
            "and (:traineeName is null or lower(t.trainee.username) = :traineeName)")
    List<Training> getTrainingsList(
            @Param("username") String username,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("traineeName") String traineeName
    );

    @Query("select count(t) from Trainer t where t.isActive = true")
    int countActiveTrainers();
}
