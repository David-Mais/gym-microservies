package com.davidmaisuradze.gymapplication.repository;

import com.davidmaisuradze.gymapplication.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findByTrainingTypeNameIgnoreCase(String trainingTypeName);
}
