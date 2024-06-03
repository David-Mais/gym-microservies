package com.davidmaisuradze.trainerworkservice.repository;

import com.davidmaisuradze.trainerworkservice.entity.TrainerWorkSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerWorkSummaryRepository extends MongoRepository<TrainerWorkSummary, Long> {
    Optional<TrainerWorkSummary> findByUsername(String username);
}
