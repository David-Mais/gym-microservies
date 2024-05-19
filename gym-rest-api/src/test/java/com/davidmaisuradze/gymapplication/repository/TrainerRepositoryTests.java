package com.davidmaisuradze.gymapplication.repository;

import com.davidmaisuradze.gymapplication.entity.Trainee;
import com.davidmaisuradze.gymapplication.entity.Trainer;
import com.davidmaisuradze.gymapplication.entity.Training;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = "/database/test-schema.sql")
@ActiveProfiles("test")
class TrainerRepositoryTests {
    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void testFindByUsername_WhenTrainerExists_ThenReturnTrainer() {
        String username = "Merab.Dvalishvili";
        Optional<Trainer> trainer = trainerRepository.findByUsernameIgnoreCase(username);
        assertThat(trainer).isPresent();
        assertThat(trainer.get().getUsername()).isEqualTo(username);
    }

    @Test
    void testFindByUsername_WhenTrainerDoesNotExist_ThenReturnNull() {
        String username = "Not.Existing";
        Optional<Trainer> trainer = trainerRepository.findByUsernameIgnoreCase(username);
        assertThat(trainer).isEmpty();
    }

    @Test
    void testTrainersNotAssigned_WhenTraineeExists_ThenReturnTrainerList() {
        String username = "Davit.maisuradze";
        List<Trainer> notAssignedTrainers = trainerRepository.getTrainersNotAssigned(username);
        assertThat(notAssignedTrainers).isNotNull();
    }

    @Test
    void testGetAllTrainees_WhenTrainerExists_ThenReturnTraineeList() {
        String username = "Merab.Dvalishvili";
        List<Trainee> trainees = trainerRepository.getAllTrainees(username.toLowerCase());
        assertThat(trainees).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    void testGetAllTrainees_WhenTrainerDoesNotExist_ThenReturnNull() {
        String username = "Not.Existing";
        List<Trainee> trainees = trainerRepository.getAllTrainees(username);
        assertThat(trainees).isEmpty();
    }

    @Test
    void testGetTrainings_WhenTrainerExists_ThenReturnTrainingList() {
        String username = "Merab.Dvalishvili";
        List<Training> trainings = trainerRepository.getTrainingsList(
                username.toLowerCase(),
                null,
                null,
                null
        );
        assertThat(trainings).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    void testGetTrainings_WhenTrainerDoesNotExist_ThenReturnEmptyList() {
        String username = "Merab.Dvalishvili99";
        List<Training> trainings = trainerRepository.getTrainingsList(
                username,
                null,
                null,
                null
        );
        assertThat(trainings).isEmpty();
    }

    @Test
    void testCountActiveTrainers_ThenReturnNumberOfActiveTrainers() {
        int activeTrainers = trainerRepository.countActiveTrainers();
        assertThat(activeTrainers).isEqualTo(3);
    }
}
