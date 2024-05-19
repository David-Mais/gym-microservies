package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.dto.security.TokenDto;
import com.davidmaisuradze.gymapplication.dto.trainee.CreateTraineeDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileUpdateRequestDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileUpdateResponseDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingInfoDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingSearchCriteria;
import com.davidmaisuradze.gymapplication.entity.Trainee;
import com.davidmaisuradze.gymapplication.entity.Trainer;
import com.davidmaisuradze.gymapplication.entity.Training;
import com.davidmaisuradze.gymapplication.exception.GymException;
import com.davidmaisuradze.gymapplication.mapper.TraineeMapper;
import com.davidmaisuradze.gymapplication.mapper.TrainerMapper;
import com.davidmaisuradze.gymapplication.mapper.TrainingMapper;
import com.davidmaisuradze.gymapplication.repository.TraineeRepository;
import com.davidmaisuradze.gymapplication.repository.TrainerRepository;
import com.davidmaisuradze.gymapplication.repository.TrainingTypeRepository;
import com.davidmaisuradze.gymapplication.security.GymUserDetails;
import com.davidmaisuradze.gymapplication.service.impl.TraineeServiceImpl;
import com.davidmaisuradze.gymapplication.util.DetailsGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
class TraineeServiceTests {
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private DetailsGenerator detailsGenerator;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainingMapper trainingMapper;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    @Transactional
    void testCreateTrainee_WhenValidCreateDtoProvided_ThenReturnRegistrationTokenDto() {
        CreateTraineeDto createTraineeDto = new CreateTraineeDto();
        createTraineeDto.setFirstName("John");
        createTraineeDto.setLastName("Doe");

        String expectedUsername = "johndoe";
        String expectedPassword = "securepassword";
        String expectedToken = "token123";

        Trainee mockTrainee = new Trainee();
        mockTrainee.setUsername(expectedUsername);
        mockTrainee.setPassword(expectedPassword);

        TokenDto token = new TokenDto();
        token.setToken("token123");
        RegistrationResponse expected = new RegistrationResponse();
        expected.setToken(token);
        CredentialsDto credentialsDto = new CredentialsDto(expectedUsername, expectedPassword);
        expected.setCredentials(credentialsDto);

        when(detailsGenerator.generatePassword()).thenReturn(expectedPassword);
        when(detailsGenerator.generateUsername("John", "Doe")).thenReturn(expectedUsername);
        when(traineeMapper.createTraineeDtoToTrainee(createTraineeDto)).thenReturn(mockTrainee);
        when(passwordEncoder.encode(expectedPassword)).thenReturn("encodedPassword");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(mockTrainee);

        when(tokenService.register(any(GymUserDetails.class), anyString(), anyString())).thenReturn(expected);

        RegistrationResponse actual = traineeService.create(createTraineeDto);

        assertEquals(expectedUsername, actual.getCredentials().getUsername());
        assertEquals(expectedPassword, actual.getCredentials().getPassword());
        assertEquals(expectedToken, actual.getToken().getToken());

        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void testGetTraineeProfile_WhenUsernameExists_ThenReturnTrainee() {
        String username = "Some.User";
        String firstName = "firstName";
        String lastName = "lastName";
        Trainee expected = new Trainee();
        expected.setUsername(username);
        expected.setFirstName(firstName);
        expected.setLastName(lastName);

        TraineeProfileDto profileDto = new TraineeProfileDto();
        profileDto.setFirstName(firstName);
        profileDto.setLastName(lastName);

        when(traineeRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.of(expected));

        Trainee actual = traineeService.getTrainee(username);

        assertNotNull(actual);
        assertEquals(firstName, actual.getFirstName());
        assertEquals(lastName, actual.getLastName());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTraineeProfile_WhenUsernameDoesNotExist_ThenThrowGymException() {
        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(GymException.class, () -> traineeService.getProfile(""));

        assertTrue(exception.getMessage().contains("Trainee not found with username: "));
        assertEquals("404", exception.getErrorCode());
    }

    @Test
    @Transactional
    void testUpdateProfile_WhenUserExists_ThenReturnTraineeUpdateResponseDto() {
        String username = "user";
        TraineeProfileUpdateRequestDto updateRequestDto = new TraineeProfileUpdateRequestDto();

        Trainee trainee = new Trainee();

        TraineeProfileUpdateResponseDto expectedResponse = new TraineeProfileUpdateResponseDto();


        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainee));
        when(traineeMapper.traineeToUpdateResponseDto(any(Trainee.class))).thenReturn(expectedResponse);
        when(traineeRepository.getAllTrainers(anyString())).thenReturn(List.of(new Trainer()));

        TraineeProfileUpdateResponseDto actualResponse = traineeService.updateProfile(username, updateRequestDto);

        assertEquals(expectedResponse, actualResponse);
        assertNotNull(actualResponse.getTrainersList());
        assertFalse(actualResponse.getTrainersList().isEmpty());

        verify(traineeRepository, times(1)).save(any(Trainee.class));
        verify(trainerMapper, times(1)).trainerToTrainerInfoDto(any(Trainer.class));
    }

    @Test
    @Transactional
    void testUpdateProfile_WhenUserDoesNotExist_ThenThrowGymException() {
        String username = "user";

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        TraineeProfileUpdateRequestDto updateRequestDto = new TraineeProfileUpdateRequestDto();

        GymException exception = assertThrows(
                GymException.class,
                () -> traineeService.updateProfile(username, updateRequestDto));

        assertTrue(exception.getMessage().contains("Trainee not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    @Transactional
    void testDeleteTrainee_WhenTraineeExists() {
        String username = "user";
        Trainee traineeToDelete = new Trainee();
        traineeToDelete.setUsername(username);

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(traineeToDelete));

        traineeService.deleteByUsername(username);

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
        verify(traineeRepository, times(1)).delete(traineeToDelete);
    }

    @Test
    @Transactional
    void testDeleteTrainee_WhenTraineeDoesNotExist_ThenThrowGymException() {
        String username = "Non.Existing";

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> traineeService.deleteByUsername(username)
        );

        assertTrue(exception.getMessage().contains("Trainee not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    @Transactional
    void testUpdateActiveStatus() {
        ActiveStatusDto activeStatusDto = new ActiveStatusDto();
        activeStatusDto.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setIsActive(false);

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainee));

        traineeService.updateActiveStatus(anyString(), activeStatusDto);

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
        assertEquals(true, trainee.getIsActive());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    @Transactional
    void testUpdateActiveStatus_WhenTraineeDoesNotExist_ThenThrowGymException() {
        String username = "Non.Existing";
        ActiveStatusDto statusDto = new ActiveStatusDto();

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> traineeService.updateActiveStatus(username, statusDto)
        );

        assertTrue(exception.getMessage().contains("Trainee not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTrainingsList_WhenTraineeExists_ThenReturnTrainingsList() {
        String username = "user";
        TrainingSearchCriteria criteria = new TrainingSearchCriteria();
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        Training training = new Training();
        training.setTrainer(trainer);

        List<Training> trainings = List.of(training);

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainee));
        when(traineeRepository.getTrainingsList(eq(username), isNull(), isNull(), isNull(), isNull())).thenReturn(trainings);
        when(trainingMapper.trainingToTrainingInfoDto(any(Training.class))).thenReturn(new TrainingInfoDto());

        List<TrainingInfoDto> trainingDtos = traineeService.getTrainingsList(username, criteria);

        assertNotNull(trainingDtos);
        assertFalse(trainingDtos.isEmpty());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(username);
        verify(traineeRepository, times(1)).getTrainingsList(eq(username), isNull(), isNull(), isNull(), isNull());
    }

    @Test
    void testGetTrainingsList_WhenTrainingTypeDoesNotExist_ThenThrowGymException() {
        String username = "user";
        TrainingSearchCriteria criteria = new TrainingSearchCriteria();
        criteria.setTrainingTypeName("nonexisting");
        Trainee trainee = new Trainee();

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainee));
        when(trainingTypeRepository.findByTrainingTypeNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> traineeService.getTrainingsList(username, criteria)
        );

        assertTrue(exception.getMessage().contains("Training type not found with name: "));
        assertEquals("404", exception.getErrorCode());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTrainingsList_WhenTrainerDoesNotExist_ThenThrowGymException() {
        String username = "user";
        TrainingSearchCriteria criteria = new TrainingSearchCriteria();
        criteria.setName("nonexisting");
        Trainee trainee = new Trainee();

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> traineeService.getTrainingsList(username, criteria)
        );

        assertTrue(exception.getMessage().contains("Trainer not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTrainee_WhenTraineeExists_ThenReturnTrainee() {
        String username = "user";
        Trainee expected = new Trainee();
        expected.setUsername(username);

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(expected));

        Trainee actual = traineeService.getTrainee(username);

        assertNotNull(actual);
        assertEquals(username, actual.getUsername());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }

    @Test
    void testGetTrainee_WhenTraineeDoesNotExist_ThenThrowGymException() {
        String username = "user";
        Trainee expected = new Trainee();
        expected.setUsername(username);

        when(traineeRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        GymException exception = assertThrows(
                GymException.class,
                () -> traineeService.getTrainee(username)
        );

        assertTrue(exception.getMessage().contains("Trainee not found with username: "));
        assertEquals("404", exception.getErrorCode());

        verify(traineeRepository, times(1)).findByUsernameIgnoreCase(anyString());
    }
}
