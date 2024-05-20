package com.davidmaisuradze.gymapplication.service.impl;

import com.davidmaisuradze.gymapplication.dto.training.CreateTrainingDto;
import com.davidmaisuradze.gymapplication.dto.workload.ActionType;
import com.davidmaisuradze.gymapplication.dto.workload.TrainerWorkloadRequestDto;
import com.davidmaisuradze.gymapplication.entity.Trainee;
import com.davidmaisuradze.gymapplication.entity.Trainer;
import com.davidmaisuradze.gymapplication.entity.Training;
import com.davidmaisuradze.gymapplication.entity.TrainingType;
import com.davidmaisuradze.gymapplication.repository.TrainingRepository;
import com.davidmaisuradze.gymapplication.service.TraineeService;
import com.davidmaisuradze.gymapplication.service.TrainerService;
import com.davidmaisuradze.gymapplication.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingRepository trainingRepository;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public void create(CreateTrainingDto createTrainingDto) {
        Trainee trainee = findTraineeProfileByUsername(createTrainingDto.getTraineeUsername());
        Trainer trainer = findTrainerProfileByUsername(createTrainingDto.getTrainerUsername());
        String name = createTrainingDto.getTrainingName();
        TrainingType trainingType = trainer.getSpecialization();
        LocalDate trainingDate = createTrainingDto.getTrainingDate();
        Integer duration = createTrainingDto.getDuration();

        Training training = Training
                .builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(name)
                .trainingType(trainingType)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();

        trainingRepository.save(training);

        String username = trainer.getUsername();
        TrainerWorkloadRequestDto workloadRequestDto = TrainerWorkloadRequestDto
                .builder()
                .username(username)
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .isActive(trainer.getIsActive())
                .trainingDate(trainingDate)
                .durationMinutes(duration)
                .actionType(ActionType.ADD)
                .build();
        String workloadServiceUrl = "http://localhost:8081/api/v1/trainers/" + username;
        restTemplate.postForEntity(workloadServiceUrl, workloadRequestDto, Void.class);
    }

    private Trainee findTraineeProfileByUsername(String username) {
        return traineeService.getTrainee(username);
    }

    private Trainer findTrainerProfileByUsername(String username) {
        return trainerService.getTrainer(username);
    }
}
