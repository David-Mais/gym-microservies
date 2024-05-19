package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.dto.security.TokenDto;
import com.davidmaisuradze.gymapplication.dto.trainer.CreateTrainerDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerInfoDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileUpdateRequestDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileUpdateResponseDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerTrainingSearchDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingInfoDto;
import com.davidmaisuradze.gymapplication.dto.trainingtype.TrainingTypeDto;
import com.davidmaisuradze.gymapplication.entity.Trainee;
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
import com.davidmaisuradze.gymapplication.service.impl.TrainerServiceImpl;
import com.davidmaisuradze.gymapplication.util.DetailsGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private DetailsGenerator detailsGenerator;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainingMapper trainingMapper;
    @Mock
    private TrainingTypeMapper trainingTypeMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private TrainerServiceImpl trainerService;


    @Test
    @Transactional
    void testCreateTrainer_WhenCreateDtoProvided_ThenReturnRegistrationTokenDto() {
        CreateTrainerDto createTrainerDto = new CreateTrainerDto();
        createTrainerDto.setFirstName("John");
        createTrainerDto.setLastName("Doe");
        TrainingTypeDto specialization = new TrainingTypeDto();
        String trainingType = "Yoga";
        specialization.setTrainingTypeName(trainingType);
        createTrainerDto.setSpecialization(specialization);

        String expectedUsername = "johndoe";
        String expectedPassword = "securepassword";
        String expectedToken = "token123";

        Trainer mockTrainer = new Trainer();
        mockTrainer.setUsername(expectedUsername);
        mockTrainer.setPassword(expectedPassword);

        TokenDto token = new TokenDto();
        token.setToken("token123");
        RegistrationResponse expected = new RegistrationResponse();
        expected.setToken(token);
        CredentialsDto credentialsDto = new CredentialsDto(expectedUsername, expectedPassword);
        expected.setCredentials(credentialsDto);

        when(detailsGenerator.generateUsername(anyString(), anyString())).thenReturn(expectedUsername);
        when(detailsGenerator.generatePassword()).thenReturn(expectedPassword);
        when(trainerMapper.createTrainerDtoToTrainer(any(CreateTrainerDto.class))).thenReturn(mockTrainer);
        when(passwordEncoder.encode(expectedPassword)).thenReturn("encodedPassword");
        when(trainerRepository.save(any(Trainer.class))).thenReturn(mockTrainer);
        when(trainingTypeRepository.findByTrainingTypeNameIgnoreCase(anyString())).thenReturn(Optional.of(new TrainingType()));

        when(tokenService.register(any(GymUserDetails.class), anyString(), anyString())).thenReturn(expected);

        RegistrationResponse actual = trainerService.create(createTrainerDto);

        assertNotNull(actual);
        assertEquals(expectedUsername, actual.getCredentials().getUsername());
        assertEquals(expectedPassword, actual.getCredentials().getPassword());
        assertEquals(expectedToken, actual.getToken().getToken());

        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }


    @Test
    @Transactional
    void testCreateTrainer_WhenInvalidCreateDtoProvided_ThenThrowGymException() {
        CreateTrainerDto createTrainerDto = new CreateTrainerDto();

        createTrainerDto.setFirstName(null);
        createTrainerDto.setLastName("lastName");

        TrainingTypeDto specialization = new TrainingTypeDto();
        specialization.setTrainingTypeName("specialization");
        createTrainerDto.setSpecialization(specialization);

        when(detailsGenerator.generatePassword()).thenReturn(null);
        when(detailsGenerator.generateUsername(null, "lastName")).thenReturn(null);
        when(trainerMapper.createTrainerDtoToTrainer(any(CreateTrainerDto.class))).thenReturn(new Trainer());
        when(trainingTypeRepository.findByTrainingTypeNameIgnoreCase(anyString())).thenReturn(Optional.empty());


        assertThrows(GymException.class, () -> trainerService.create(createTrainerDto));
    }

    @Test
    void testGetTrainerProfile_WhenTrainerExists_ThenReturnProfile() {
        String username = "user";
        Trainer trainer = new Trainer();
        trainer.setFirstName("firstName");
        trainer.setUsername(username);

        TrainerProfileDto trainerProfileDto = new TrainerProfileDto();
        trainerProfileDto.setFirstName("firstName");


        when(trainerRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(trainer));
        when(trainerMapper.trainerToTrainerProfileDto(any(Trainer.class))).thenReturn(trainerProfileDto);
        when(trainerRepository.getAllTrainees(anyString())).thenReturn(new ArrayList<>());

        TrainerProfileDto actual = trainerService.getProfile(username);

        assertEquals("firstName", actual.getFirstName());
        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTrainerProfile_WhenUsernameDoesNotExist_ThenThrowGymException() {
        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> trainerService.getProfile("Non.Existing")
        );

        assertTrue(exception.getMessage().contains("Trainer not found with username: "));
        assertEquals("404", exception.getErrorCode());
    }

    @Test
    @Transactional
    void testUpdateTrainerProfile_WhenTrainerExists_ThenUpdateProfile() {
        String username = "user";
        TrainerProfileUpdateRequestDto updateRequestDto = new TrainerProfileUpdateRequestDto();

        Trainer trainer = new Trainer();

        TrainerProfileUpdateResponseDto expectedResponse = new TrainerProfileUpdateResponseDto();


        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainer));
        when(trainerMapper.trainerToUpdateResponseDto(any(Trainer.class))).thenReturn(expectedResponse);
        when(trainerRepository.getAllTrainees(anyString())).thenReturn(List.of(new Trainee()));

        TrainerProfileUpdateResponseDto actualResponse = trainerService.updateProfile(username, updateRequestDto);

        assertEquals(expectedResponse, actualResponse);
        assertNotNull(actualResponse.getTraineesList());
        assertFalse(actualResponse.getTraineesList().isEmpty());

        verify(trainerRepository, times(1)).save(any(Trainer.class));
        verify(traineeMapper, times(1)).traineeToTraineeInfoDto(any(Trainee.class));
    }

    @Test
    @Transactional
    void testUpdateTrainerProfile_WhenTrainerDoesNotExist_ThenThrowGymException() {
        String username = "trainer";

        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        TrainerProfileUpdateRequestDto updateRequestDto = new TrainerProfileUpdateRequestDto();

        GymException exception = assertThrows(
                GymException.class,
                () -> trainerService.updateProfile(username, updateRequestDto));

        assertTrue(exception.getMessage().contains("Trainer not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    @Transactional
    void testUpdateActiveStatus_WhenTrainerExists_ThenUpdateActiveStatus() {
        ActiveStatusDto activeStatusDto = new ActiveStatusDto();
        activeStatusDto.setIsActive(true);

        Trainer trainer = new Trainer();
        trainer.setIsActive(false);

        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainer));

        trainerService.updateActiveStatus(anyString(), activeStatusDto);

        assertEquals(true, trainer.getIsActive());
        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(anyString());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    @Transactional
    void testUpdateActiveStatus_WhenTrainerDoesNotExist_ThenThrowGymException() {
        String username = "Non.Existing";
        ActiveStatusDto statusDto = new ActiveStatusDto();
        statusDto.setIsActive(true);

        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> trainerService.updateActiveStatus(username, statusDto)
        );

        assertTrue(exception.getMessage().contains("Trainer not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testTrainersNotAssigned_WhenTraineeExists_ThenReturnListOfTrainerInfoDto() {
        Trainee trainee = new Trainee();

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainee));
        when(trainerRepository.getTrainersNotAssigned(anyString())).thenReturn(List.of(new Trainer()));
        when(trainerMapper.trainerToTrainerInfoDto(any(Trainer.class))).thenReturn(new TrainerInfoDto());

        List<TrainerInfoDto> actual = trainerService.getTrainersNotAssigned(anyString());

        assertFalse(actual.isEmpty());
    }

    @Test
    void testTrainersNotAssigned_WhenTraineeDoesNotExist_ThenThrowGymException() {
        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());


        GymException exception = assertThrows(
                GymException.class,
                () -> trainerService.getTrainersNotAssigned("Non.Existing")
        );

        assertTrue(exception.getMessage().contains("Trainer not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTrainingsList_WhenTrainerExists_ThenReturnListOfTrainerInfoDto() {
        String username = "user";
        TrainerTrainingSearchDto criteria = new TrainerTrainingSearchDto();
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(TrainingType.builder().trainingTypeName("spec").build());

        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(trainee);

        List<Training> trainings = List.of(training);

        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainer));
        when(trainerRepository.getTrainingsList(eq(username), isNull(), isNull(), isNull())).thenReturn(trainings);
        when(trainingMapper.trainingToTrainingInfoDto(any(Training.class))).thenReturn(new TrainingInfoDto());
        when(trainingTypeMapper.entityToDto(any(TrainingType.class))).thenReturn(new TrainingTypeDto());

        List<TrainingInfoDto> trainingDtos = trainerService.getTrainingsList(username, criteria);

        assertNotNull(trainingDtos);
        assertFalse(trainingDtos.isEmpty());

        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(username);
        verify(trainerRepository, times(1)).getTrainingsList(eq(username), isNull(), isNull(), isNull());
    }

    @Test
    void testGetTrainingsList_WhenTrainerDoesNotExist_ThenThrowGymException() {
        String username = "user";
        TrainerTrainingSearchDto criteria = new TrainerTrainingSearchDto();
        criteria.setName("nonexisting");

        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> trainerService.getTrainingsList(username, criteria)
        );

        assertTrue(exception.getMessage().contains("Trainer not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTrainer_WhenTrainerExists_ThenReturnTrainer() {
        String username = "user";
        Trainer expected = new Trainer();
        expected.setUsername(username);

        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(expected));

        Trainer actual = trainerService.getTrainer(username);

        assertNotNull(actual);
        assertEquals(username, actual.getUsername());

        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTrainer_WhenTrainerDoesNotExist_ThenThrowGymException() {
        String username = "Non.Existing";

        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> trainerService.getTrainer(username)
        );

        assertTrue(exception.getMessage().contains("Trainer not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }
}
