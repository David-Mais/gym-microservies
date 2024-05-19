package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.training.CreateTrainingDto;
import com.davidmaisuradze.gymapplication.entity.Trainee;
import com.davidmaisuradze.gymapplication.entity.Trainer;
import com.davidmaisuradze.gymapplication.entity.Training;
import com.davidmaisuradze.gymapplication.entity.TrainingType;
import com.davidmaisuradze.gymapplication.exception.GymException;
import com.davidmaisuradze.gymapplication.repository.TrainingRepository;
import com.davidmaisuradze.gymapplication.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTests {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingRepository trainingRepository;
    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    @Transactional
    void createTraining() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(TrainingType.builder().trainingTypeName("yoga").build());

        String traineeName = "traineeName";
        String trainerName = "trainerName";
        String trainingName = "trainingName";
        LocalDate trainingDate = LocalDate.of(2000, 1, 1);
        Integer duration = 30;

        CreateTrainingDto training = CreateTrainingDto
                .builder()
                .traineeUsername(traineeName)
                .trainerUsername(trainerName)
                .trainingName(trainingName)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();

        when(traineeService.getTrainee(anyString())).thenReturn(trainee);
        when(trainerService.getTrainer(anyString())).thenReturn(trainer);

        trainingService.create(training);

        ArgumentCaptor<Training> trainingCaptor = ArgumentCaptor.forClass(Training.class);
        verify(trainingRepository).save(trainingCaptor.capture());

        Training capturedTraining = trainingCaptor.getValue();

        assertEquals("trainingName", capturedTraining.getTrainingName());
        assertEquals("yoga", trainer.getSpecialization().getTrainingTypeName());
        assertEquals(trainingDate, capturedTraining.getTrainingDate());
        assertEquals(duration, capturedTraining.getDuration());
        assertSame(trainee, capturedTraining.getTrainee());
        assertSame(trainer, capturedTraining.getTrainer());
    }

    @Test
    @Transactional
    void createTraining_WhenTrainerDoesNotExist_ThenThrowException() {
        String traineeName = "traineeName";
        String trainerName = "nonExistentTrainer";
        String trainingName = "trainingName";
        LocalDate trainingDate = LocalDate.of(2000, 1, 1);
        Integer duration = 30;

        CreateTrainingDto trainingDto = CreateTrainingDto.builder()
                .traineeUsername(traineeName)
                .trainerUsername(trainerName)
                .trainingName(trainingName)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();

        when(trainerService.getTrainer(anyString()))
                .thenThrow(new GymException("Trainer not found with username: " + trainerName, "404"));

        GymException exception = assertThrows(
                GymException.class,
                () -> trainingService.create(trainingDto)
        );

        assertTrue(exception.getMessage().contains("Trainer not found with username: "));
        assertEquals("404", exception.getErrorCode());
    }
}
