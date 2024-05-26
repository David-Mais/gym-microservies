package com.davidmaisuradze.gymapplication.service.impl;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.dto.trainee.CreateTraineeDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileUpdateRequestDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileUpdateResponseDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerInfoDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingInfoDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingSearchCriteria;
import com.davidmaisuradze.gymapplication.dto.workload.ActionType;
import com.davidmaisuradze.gymapplication.dto.workload.TrainerWorkloadRequest;
import com.davidmaisuradze.gymapplication.entity.Trainee;
import com.davidmaisuradze.gymapplication.entity.Training;
import com.davidmaisuradze.gymapplication.exception.GymException;
import com.davidmaisuradze.gymapplication.mapper.TraineeMapper;
import com.davidmaisuradze.gymapplication.mapper.TrainerMapper;
import com.davidmaisuradze.gymapplication.mapper.TrainingMapper;
import com.davidmaisuradze.gymapplication.mapper.TrainingTypeMapper;
import com.davidmaisuradze.gymapplication.message.WorkloadMessageSender;
import com.davidmaisuradze.gymapplication.repository.TraineeRepository;
import com.davidmaisuradze.gymapplication.repository.TrainerRepository;
import com.davidmaisuradze.gymapplication.repository.TrainingTypeRepository;
import com.davidmaisuradze.gymapplication.security.GymUserDetails;
import com.davidmaisuradze.gymapplication.service.TokenService;
import com.davidmaisuradze.gymapplication.service.TraineeService;
import com.davidmaisuradze.gymapplication.util.DetailsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final DetailsGenerator detailsGenerator;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    private final TrainingTypeMapper trainingTypeMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final WorkloadMessageSender workloadMessageSender;

    @Override
    @Transactional
    public RegistrationResponse create(CreateTraineeDto createTraineeDto) {
        String password = detailsGenerator.generatePassword();
        String username = detailsGenerator.generateUsername(
                createTraineeDto.getFirstName(),
                createTraineeDto.getLastName()
        );

        Trainee trainee = traineeMapper.createTraineeDtoToTrainee(createTraineeDto);

        trainee.setPassword(passwordEncoder.encode(password));
        trainee.setUsername(username);
        trainee.setIsActive(true);

        trainee = traineeRepository.save(trainee);

        log.info("Trainee Created");

        GymUserDetails user = new GymUserDetails(trainee);
        return tokenService.register(user, username, password);
    }

    @Override
    @PreAuthorize("#username == authentication.principal.username")
    public TraineeProfileDto getProfile(String username) {
        Trainee trainee = getTrainee(username);
        TraineeProfileDto profileDto = traineeMapper.traineeToTraineeProfileDto(trainee);
        profileDto.setTrainersList(getAllTrainerDto(username));

        return profileDto;
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.principal.username")
    public TraineeProfileUpdateResponseDto updateProfile(
            String username,
            TraineeProfileUpdateRequestDto updateRequestDto
    ) {
        Trainee trainee = getTrainee(username);

        updateTraineeProfile(trainee, updateRequestDto);

        TraineeProfileUpdateResponseDto responseDto = traineeMapper.traineeToUpdateResponseDto(trainee);

        responseDto.setTrainersList(getAllTrainerDto(username));

        return responseDto;
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.principal.username")
    public void deleteByUsername(String username) {
        removeAllFutureTrainings(username);

        Trainee traineeToDelete = getTrainee(username);
        traineeRepository.delete(traineeToDelete);
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.principal.username")
    public void updateActiveStatus(String username, ActiveStatusDto activeStatusDto) {
        Trainee trainee = getTrainee(username);
        trainee.setIsActive(activeStatusDto.getIsActive());
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.principal.username")
    public List<TrainingInfoDto> getTrainingsList(String username, TrainingSearchCriteria criteria) {
        trainingSearchValidator(username, criteria);

        String trainerUsername = criteria.getName();
        String trainingTypeName = criteria.getTrainingTypeName();
        if (trainerUsername != null) {
            trainerUsername = trainerUsername.toLowerCase();
        }
        if (trainingTypeName != null) {
            trainingTypeName = trainingTypeName.toLowerCase();
        }

        List<Training> trainings = traineeRepository.getTrainingsList(
                username.toLowerCase(),
                criteria.getFrom(),
                criteria.getTo(),
                trainerUsername,
                trainingTypeName
        );


        return trainings.stream()
                .map(t -> {
                    TrainingInfoDto trainingInfo = trainingMapper.trainingToTrainingInfoDto(t);
                    trainingInfo.setUsername(t.getTrainer().getUsername());

                    if (criteria.getTrainingTypeName() != null) {
                        trainingTypeRepository.findByTrainingTypeNameIgnoreCase(criteria.getTrainingTypeName())
                                .ifPresent(trainingType -> trainingInfo.setTrainingType(
                                        trainingTypeMapper.entityToDto(trainingType)
                                )
                                );
                    }
                    return trainingInfo;
                })
                .toList();
    }

    @Override
    public Trainee getTrainee(String username) {
        return traineeRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new GymException("Trainee not found with username: " + username, "404"));
    }

    public List<TrainerInfoDto> getAllTrainerDto(String username) {
        return traineeRepository
                .getAllTrainers(username.toLowerCase())
                .stream()
                .map(trainerMapper::trainerToTrainerInfoDto)
                .toList();
    }

    private void updateTraineeProfile(Trainee trainee, TraineeProfileUpdateRequestDto updateRequestDto) {
        String firstName = updateRequestDto.getFirstName();
        String lastName = updateRequestDto.getLastName();
        LocalDate dob = updateRequestDto.getDateOfBirth();
        String address = updateRequestDto.getAddress();
        Boolean isActive = updateRequestDto.getIsActive();

        if (firstName != null) {
            trainee.setFirstName(firstName);
        }
        if (lastName != null) {
            trainee.setLastName(lastName);
        }
        if (dob != null) {
            trainee.setDateOfBirth(dob);
        }
        if (address != null) {
            trainee.setAddress(address);
        }
        if (isActive != null) {
            trainee.setIsActive(isActive);
        }

        traineeRepository.save(trainee);
    }

    private boolean trainerExists(String username) {
        return trainerRepository
                .findByUsernameIgnoreCase(username.toLowerCase())
                .isPresent();
    }

    private boolean trainingTypeExists(String typeName) {
        return trainingTypeRepository
                .findByTrainingTypeNameIgnoreCase(typeName)
                .isPresent();
    }

    private void trainingSearchValidator(String username, TrainingSearchCriteria criteria) {
        getTrainee(username);

        if (criteria.getName() != null && (!trainerExists(criteria.getName()))) {
            throw new GymException("Trainer not found with username: " + criteria.getName(), "404");
        }
        if (criteria.getTrainingTypeName() != null && (!trainingTypeExists(criteria.getTrainingTypeName()))) {
            throw new GymException("Training type not found with name: " + criteria.getTrainingTypeName(), "404");
        }
    }

    private void removeAllFutureTrainings(String username) {
        LocalDate today = LocalDate.now();
        List<Training> trainings = traineeRepository.getTrainingsList(
                username.toLowerCase(),
                today,
                null,
                null,
                null
        );
        for (Training training : trainings) {
            TrainerWorkloadRequest request = trainingMapper.trainingToTrainerWorkloadRequest(training);
            request.setActionType(ActionType.DELETE);
            workloadMessageSender.sendWorkloadMessage(request);
        }
    }
}
