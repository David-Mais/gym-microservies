package com.davidmaisuradze.gymapplication.service.impl;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeInfoDto;
import com.davidmaisuradze.gymapplication.dto.trainer.CreateTrainerDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerInfoDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileUpdateRequestDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileUpdateResponseDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerTrainingSearchDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingInfoDto;
import com.davidmaisuradze.gymapplication.entity.Trainer;
import com.davidmaisuradze.gymapplication.entity.Training;
import com.davidmaisuradze.gymapplication.entity.TrainingType;
import com.davidmaisuradze.gymapplication.exception.GymException;
import com.davidmaisuradze.gymapplication.mapper.TraineeMapper;
import com.davidmaisuradze.gymapplication.mapper.TrainerMapper;
import com.davidmaisuradze.gymapplication.mapper.TrainingMapper;
import com.davidmaisuradze.gymapplication.mapper.TrainingTypeMapper;
import com.davidmaisuradze.gymapplication.repository.TraineeRepository;
import com.davidmaisuradze.gymapplication.repository.TrainerRepository;
import com.davidmaisuradze.gymapplication.repository.TrainingTypeRepository;
import com.davidmaisuradze.gymapplication.security.GymUserDetails;
import com.davidmaisuradze.gymapplication.service.TokenService;
import com.davidmaisuradze.gymapplication.service.TrainerService;
import com.davidmaisuradze.gymapplication.util.DetailsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final DetailsGenerator detailsGenerator;
    private final TrainerMapper trainerMapper;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TrainingTypeMapper trainingTypeMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    @Transactional
    public RegistrationResponse create(CreateTrainerDto createTrainerDto) {
        String password = detailsGenerator.generatePassword();
        String username = detailsGenerator.generateUsername(
                createTrainerDto.getFirstName(),
                createTrainerDto.getLastName()
        );

        Trainer trainer = trainerMapper.createTrainerDtoToTrainer(createTrainerDto);
        TrainingType specialization = findTrainingTypeByName(createTrainerDto.getSpecialization().getTrainingTypeName());

        trainer.setPassword(passwordEncoder.encode(password));
        trainer.setUsername(username);
        trainer.setIsActive(true);
        trainer.setSpecialization(specialization);
        trainer = trainerRepository.save(trainer);

        log.info("Trainer Created");

        GymUserDetails user = new GymUserDetails(trainer);
        return tokenService.register(user, username, password);
    }

    @Override
//    @PreAuthorize("#username == authentication.principal.username")
    public TrainerProfileDto getProfile(String username) {
        Trainer trainer = getTrainer(username);
        TrainerProfileDto trainerProfile = trainerMapper.trainerToTrainerProfileDto(trainer);

        trainerProfile.setTraineesList(getAllTraineeInfoDto(username));

        return trainerProfile;
    }

    @Override
    @Transactional
//    @PreAuthorize("#username == authentication.principal.username")
    public TrainerProfileUpdateResponseDto updateProfile(
            String username,
            TrainerProfileUpdateRequestDto requestDto
    ) {
        Trainer trainer = getTrainer(username);

        updateProfileHelper(trainer, requestDto);

        TrainerProfileUpdateResponseDto responseDto = trainerMapper.trainerToUpdateResponseDto(trainer);

        responseDto.setTraineesList(getAllTraineeInfoDto(username));

        return responseDto;
    }

    @Override
    @Transactional
//    @PreAuthorize("#username == authentication.principal.username")
    public void updateActiveStatus(String username, ActiveStatusDto activeStatusDto) {
        boolean isActive = activeStatusDto.getIsActive();
        Trainer trainer = getTrainer(username);
        trainer.setIsActive(isActive);
        trainerRepository.save(trainer);
        log.info("Trainer={} isActive={}", username, isActive);
    }

    @Override
//    @PreAuthorize("#username == authentication.principal.username")
    public List<TrainerInfoDto> getTrainersNotAssigned(String username) {
        if (traineeNotExists(username)) {
            throw new GymException("Trainer not found with username: " + username, "404");
        }
        return trainerRepository
                .getTrainersNotAssigned(username)
                .stream()
                .map(trainerMapper::trainerToTrainerInfoDto)
                .toList();
    }

    @Override
//    @PreAuthorize("#username == authentication.principal.username")
    public List<TrainingInfoDto> getTrainingsList(String username, TrainerTrainingSearchDto criteria) {
        trainingSearchValidator(username, criteria);

        String traineeUsername = criteria.getName();
        if (traineeUsername != null) {
            traineeUsername = traineeUsername.toLowerCase();
        }

        List<Training> trainings = trainerRepository.getTrainingsList(
                username.toLowerCase(),
                criteria.getFrom(),
                criteria.getTo(),
                traineeUsername
        );

        List<TrainingInfoDto> trainingInfoDtos = new ArrayList<>();

        for (Training t : trainings) {
            TrainingInfoDto trainingInfo = trainingMapper.trainingToTrainingInfoDto(t);
            trainingInfo.setUsername(t.getTrainee().getUsername());
            trainingInfo.setTrainingType(trainingTypeMapper.entityToDto(t.getTrainer().getSpecialization()));
            trainingInfoDtos.add(trainingInfo);
        }

        return trainingInfoDtos;
    }

    @Override
    public Trainer getTrainer(String username) {
        return trainerRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new GymException("Trainer not found with username: " + username, "404"));
    }

    private List<TraineeInfoDto> getAllTraineeInfoDto(String username) {
        return trainerRepository.getAllTrainees(username.toLowerCase())
                .stream()
                .map(traineeMapper::traineeToTraineeInfoDto)
                .toList();
    }

    private TrainingType findTrainingTypeByName(String typeName) {
        Optional<TrainingType> type = trainingTypeRepository.findByTrainingTypeNameIgnoreCase(typeName);
        if (type.isEmpty()) {
            throw new GymException("Training type not found", "404");
        }
        return type.get();
    }

    private void updateProfileHelper(Trainer trainer, TrainerProfileUpdateRequestDto requestDto) {
        String firstName = requestDto.getFirstName();
        String lastName = requestDto.getLastName();
        Boolean isActive = requestDto.getIsActive();

        if (firstName != null) {
            trainer.setFirstName(firstName);
        }
        if (lastName != null) {
            trainer.setLastName(lastName);
        }
        if (isActive != null) {
            trainer.setIsActive(isActive);
        }

        trainerRepository.save(trainer);
    }

    private boolean traineeNotExists(String username) {
        return traineeRepository.findByUsernameIgnoreCase(username).isEmpty();
    }

    private void trainingSearchValidator(String username, TrainerTrainingSearchDto criteria) {
        getTrainer(username);

        if (criteria.getName() != null && (traineeNotExists(criteria.getName()))) {
            throw new GymException("Trainee not found with username: " + criteria.getName(), "404");
        }
    }
}
