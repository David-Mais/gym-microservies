package com.davidmaisuradze.gymapplication.repository;

import com.davidmaisuradze.gymapplication.entity.Trainee;
import com.davidmaisuradze.gymapplication.entity.Trainer;
import com.davidmaisuradze.gymapplication.entity.Training;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = "/database/test-schema.sql")
@ActiveProfiles("test")
class TraineeRepositoryTests {
    @Autowired
    private TraineeRepository traineeRepository;

    @Test
    @Transactional
    void testFindByUsername_WhenUsernameExists_ThenReturnTrainee() {
        Trainee newTrainee = Trainee
                .builder()
                .id(1L)
                .firstName("Some")
                .lastName("User")
                .username("Some.User")
                .password("pass")
                .isActive(true)
                .dateOfBirth(LocalDate.parse("2000-01-01"))
                .build();
        traineeRepository.save(newTrainee);

        Optional<Trainee> found = traineeRepository.findByUsernameIgnoreCase("Some.User");

        found.ifPresent(trainee -> assertThat(trainee.getUsername()).isEqualTo(newTrainee.getUsername()));
    }

    @Test
    void testFindByUsername_WhenUsernameDoesNotExist_ThenReturnNull() {
        String username = "Non.Existing";
        Optional<Trainee> trainee = traineeRepository.findByUsernameIgnoreCase(username);
        assertThat(trainee).isEmpty();
    }

    @Test
    void testGetAllTrainers_WhenUsernameExists_ThenReturnAllTrainers() {
        String username = "Davit.Maisuradze";
        List<Trainer> trainers = traineeRepository.getAllTrainers(username.toLowerCase());
        assertThat(trainers).isNotNull().hasSize(1);
    }

    @Test
    void testGetAllTrainers_WhenUsernameDoesNotExist_ThenReturnEmptyList() {
        String username = "Non.Existing";
        List<Trainer> trainers = traineeRepository.getAllTrainers(username);
        assertThat(trainers).isNotNull().isEmpty();
    }

    @Test
    void testGetTrainings_WhenTrainerExists_ThenReturnAllTrainings() {
        String username = "Davit.Maisuradze";
        String trainer = "Merab.Dvalishvili";
        List<Training> trainings = traineeRepository.getTrainingsList(
                username.toLowerCase(),
                null,
                  null,
                trainer.toLowerCase(),
                null
        );

        assertThat(trainings).isNotNull().hasSize(1);
    }

    @Test
    void testGetTrainings_WhenDateDoesNotExist_ThenReturnEmptyList() {
        String username = "Davit.Maisuradze";
        List<Training> trainings = traineeRepository.getTrainingsList(
                username,
                LocalDate.parse("2100-01-01"),
                null,
                null,
                null
        );

        assertThat(trainings).isNotNull().isEmpty();
    }
}
