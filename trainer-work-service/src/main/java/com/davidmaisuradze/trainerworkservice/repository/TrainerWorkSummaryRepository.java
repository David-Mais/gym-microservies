package com.davidmaisuradze.trainerworkservice.repository;

import com.davidmaisuradze.trainerworkservice.entity.TrainerWorkSummary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerWorkSummaryRepository extends JpaRepository<TrainerWorkSummary, Long> {
    @EntityGraph(attributePaths = {"yearlySummaries", "yearlySummaries.monthlySummaries"})
    Optional<TrainerWorkSummary> findByUsername(String username);
    @EntityGraph(attributePaths = {"yearlySummaries", "yearlySummaries.monthlySummaries"})
    List<TrainerWorkSummary> findAll();
}
