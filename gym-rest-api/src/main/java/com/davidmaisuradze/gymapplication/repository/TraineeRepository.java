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
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByUsernameIgnoreCase(String username);

    @Query("select t.trainer from Training t where lower(t.trainee.username) = :username")
    List<Trainer> getAllTrainers(@Param("username") String username);

    @Query("select t from Training t where lower(t.trainee.username) = :username " +
            "and (:fromDate is null or t.trainingDate >= :fromDate) " +
            "and (:toDate is null or t.trainingDate <= :toDate) " +
            "and (:trainerName is null or lower(t.trainer.username) = :trainerName) " +
            "and (:trainingTypeName is null or lower(t.trainingType.trainingTypeName) = :trainingTypeName)")
    List<Training> getTrainingsList(
            @Param("username") String username,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingTypeName") String trainingTypeName
    );
}
