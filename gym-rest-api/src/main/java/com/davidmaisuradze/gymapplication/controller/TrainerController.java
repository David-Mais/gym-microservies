package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.ErrorDto;
import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.dto.trainer.CreateTrainerDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerInfoDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileUpdateRequestDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileUpdateResponseDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerTrainingSearchDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingInfoDto;
import com.davidmaisuradze.gymapplication.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainers", description = "Endpoint for managing Trainers")
public class TrainerController {
    private final TrainerService trainerService;

    @PostMapping()
    @Operation(
            description = "Endpoint for registering Trainers with provided details",
            summary = "Register new Trainer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateTrainerDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Trainer registration with all fields",
                                            description = "All fields that are possible to provide during " +
                                                    "registration, since Username and Password will be generated " +
                                                    "during creation there is no need to provide them here.",
                                            value = """
                                                    {
                                                      "firstName": "Jack",
                                                      "lastName": "Sparrow",
                                                      "specialization": {
                                                        "trainingTypeName": "dance"
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Illegal registration",
                                            description = "This is illegal since without specialization it is not +" +
                                                    "possible to create Trainer in this API.",
                                            value = """
                                                    {
                                                      "firstName": "Jack",
                                                      "lastName": "Sparrow"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "**Trainer created successfully**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CredentialsDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Trainer registered successfully",
                                                    description = "When trainer is registered successfully " +
                                                            "their newly generated Username and Password are " +
                                                            "provided using CredentialsDto",
                                                    value = """
                                                            {
                                                              "credentials": {
                                                                "username": "Jack.Sparrow",
                                                                "password": "db72e695"
                                                              },
                                                              "token": {
                                                                "id": 1,
                                                                "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKYWNrLlNwYXJyb3ciLCJpYXQiOjE3MTQ0NzA3MTEsImV4cCI6MTcxNDQ5MDcxMX0.RNNUCqIhyLKgXNiaQ5J77DM-euE4TcD869G3Wqbm0MI",
                                                                "username": "Jack.Sparrow",
                                                                "expiredAt": "2024-04-30T15:25:11.000+00:00"
                                                              }
                                                            }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Trainer registered successfully II",
                                                    description = "When trainer is registered successfully " +
                                                            "their newly generated Username and Password are " +
                                                            "provided using CredentialsDto",
                                                    value = """
                                                            {
                                                              "credentials": {
                                                                "username": "Jon.Jones",
                                                                "password": "asdfbhnj"
                                                              },
                                                              "token": {
                                                                "id": 1,
                                                                "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKYWNrLlNwYXJyb3ciLCJpYXQiOjE3MTQ0NzA3MTEsImV4cCI6MTcxNDQ5MDcxMX0.RNNUCqIhyLKgXNiaQ5J77DM-euE4TcD869G3Wqbm0MI",
                                                                "username": "Jon.Jones",
                                                                "expiredAt": "2024-04-30T15:25:11.000+00:00"
                                                              }
                                                            }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Trainer registration error**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Bad request provided",
                                                    description = "When there is either insufficient information " +
                                                            "or type mismatch during the creation of trainee API " +
                                                            "returns ErrorDto containing error code and specific " +
                                                            "error message that helps with debugging",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                  "lastName": "Last name should not be blank",
                                                                  "firstName": "First name should not be blank"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Specialization not provided",
                                                    description = "When specialization is not provided it is " +
                                                            "not possible to create Trainee and we return " +
                                                            "status code 400.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "specialization": "Specialization should not be null"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Error finding training type**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Training type not found",
                                                    description = "When the provided training type is not available " +
                                                            "in the application API is unable to create trainer so it " +
                                                            "responds with 404 status code and an error DTO.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Training type not found",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<RegistrationResponse> createTrainer(
            @Valid @RequestBody CreateTrainerDto createTrainerDto
    ) {
        RegistrationResponse registrationResponse = trainerService.create(createTrainerDto);
        return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
    }

    @GetMapping("/profile/{username}")
    @Operation(
            description = "Endpoint for getting Trainer profile by username",
            summary = "Get Trainer profile",
            parameters = @Parameter(
                    name = "username",
                    description = "Trainer username",
                    required = true,
                    example = "Merab.Dvalishvili"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Trainer found with provided username**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TrainerProfileDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Existing username provided",
                                                    description = "When username that is provided can be " +
                                                            "found in the database then TrainerProfileDto containing " +
                                                            "all necessary information about that Trainer will be " +
                                                            "returned to user",
                                                    value = """
                                                            {
                                                              "firstName": "Merab",
                                                              "lastName": "Dvlaishvili",
                                                              "specialization": {
                                                                "id": 1,
                                                                "trainingTypeName": "box"
                                                              },
                                                              "isActive": true,
                                                              "traineesList": [
                                                                {
                                                                  "username": "Davit.Maisuradze",
                                                                  "firstName": "Davit",
                                                                  "lastName": "Maisuradze"
                                                                },
                                                                {
                                                                  "username": "John.Doe",
                                                                  "firstName": "John",
                                                                  "lastName": "Doe"
                                                                }
                                                              ]
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Trainer without trainees",
                                                    description = "When correct username is provided even if the " +
                                                            "trainer does not have trainees profile will " +
                                                            "still bre returned in the same manner.",
                                                    value = """
                                                            {
                                                              "firstName": "Jack",
                                                              "lastName": "Sparrow",
                                                              "specialization": {
                                                                "id": 2,
                                                                "trainingTypeName": "dance"
                                                              },
                                                              "isActive": false,
                                                              "traineesList": []
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Missing or invalid token",
                                                    description = "When bearer token is either missing or invalid there will be an authentication " +
                                                            "error so client will receive response code 401 alongside the following response body.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Token is missing or invalid.",
                                                              "errorCode": "401"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "No permission to access resource",
                                                    description = "When client has a token that is valid but it is for " +
                                                            "user A and tries to access information about user B with the " +
                                                            "same token there will be response code 403 and an error DTO.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Access denied",
                                                              "errorCode": "403"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Trainer not found with the provided username",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Non existing username",
                                                    description = "When client provides username that does not " +
                                                            "exist in the database the API returns error code 404 " +
                                                            "not found and a response body ErrorDto",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainer not found with username: Jon.Jones",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "No username provided",
                                                    description = "If no username is provided the API would not " +
                                                            "be able to find resource and would be forced to return " +
                                                            "error code 404",
                                                    value = """
                                                            {
                                                              "errorMessage": "Resource not found",
                                                              "details": {
                                                                "resourcePath": "api/v1/trainers/profile",
                                                                "detailMessage": "No static resource api/v1/trainers/profile."
                                                              },
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<TrainerProfileDto> getProfile(
            @PathVariable("username") String username
    ) {
        return ResponseEntity.ok(trainerService.getProfile(username));
    }

    @PutMapping("/profile/{username}")
    @Operation(
            description = "Endpoint used to update details about Trainer profile",
            summary = "Update Trainer profile",
            parameters = @Parameter(
                    name = "username",
                    description = "Username of a Trainer we want to update",
                    required = true,
                    example = "Merab.Dvalishvili"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerProfileUpdateRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Valid update request body",
                                            description = "All fields that are possible to provide during " +
                                                    "update request is provided.",
                                            value = """
                                                    {
                                                      "firstName": "Jacky",
                                                      "lastName": "Sparrow",
                                                      "specialization": {
                                                        "trainingTypeName": "dance"
                                                      },
                                                      "isActive": "true"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Not valid update request",
                                            description = "This is not a valid update request body since specialization " +
                                                    "and is active fields are required when trying to update Trainer",
                                            value = """
                                                    {
                                                      "firstName": "Example",
                                                      "lastName": "Trainer"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Trainer updated successfully**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TrainerProfileUpdateResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Trainer Profile updates successfully",
                                                    description = "When all the necessary fields are provided " +
                                                            "and the username also exists in the database then " +
                                                            "update succeeds we return status code 200 and the " +
                                                            "response body.",
                                                    value = """
                                                            {
                                                              "username": "Merab.Dvalishvili",
                                                              "firstName": "Merab",
                                                              "lastName": "Dvalishvili",
                                                              "specialization": {
                                                                "id": 1,
                                                                "trainingTypeName": "box"
                                                              },
                                                              "isActive": true,
                                                              "traineesList": [
                                                                {
                                                                  "username": "Davit.Maisuradze",
                                                                  "firstName": "Davit",
                                                                  "lastName": "Maisuradze"
                                                                },
                                                                {
                                                                  "username": "John.Doe",
                                                                  "firstName": "John",
                                                                  "lastName": "Doe"
                                                                }
                                                              ]
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Missing or invalid token",
                                                    description = "When bearer token is either missing or invalid there will be an authentication " +
                                                            "error so client will receive response code 401 alongside the following response body.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Token is missing or invalid.",
                                                              "errorCode": "401"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "No permission to access resource",
                                                    description = "When client has a token that is valid but it is for " +
                                                            "user A and tries to access information about user B with the " +
                                                            "same token there will be response code 403 and an error DTO.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Access denied",
                                                              "errorCode": "403"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Trainee update failed. Reason:*Validation*",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "firstName and lastname not provided",
                                                    description = "When first name and last name are not provided " +
                                                            "the API is unable to perform an update due to validation " +
                                                            "errors, so client gets error code 400 bad request and " +
                                                            "ErrorDto containing error message.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "firstName": "First name should not be blank",
                                                                "lastName": "Last name should not be blank"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "isActive not provided",
                                                    description = "When isActive field is not " +
                                                            "provided API is unable to perform an update since it has " +
                                                            "some validation errors, so it marks as 400 bad request.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "isActive": "IsActive field should not be null"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Trainer not found with provided username",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Username doesn't exist",
                                                    description = "When the provided username does not exist in the " +
                                                            "database the API is unable to perform an update operation " +
                                                            "since it cannot decide which Trainer to update so API returns " +
                                                            "error code 404 not found as well as ErrorDto containing " +
                                                            "detailed message.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainer not found with username: Jon.Jones",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<TrainerProfileUpdateResponseDto> updateProfile(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerProfileUpdateRequestDto trainerProfileUpdateRequestDto
    ) {
        return ResponseEntity.ok(trainerService.updateProfile(username, trainerProfileUpdateRequestDto));
    }

    @GetMapping("/not-assigned/{username}")
    @Operation(
            description = "Endpoint used to get all active trainers that are not assigned to a Specific *Trainee*.",
            summary = "Get active Trainers not assigned to Trainee",
            parameters = @Parameter(
                    name = "username",
                    description = "Username of a *Trainee* whose trainers we are not interested in.",
                    required = true,
                    example = "Davit.Maisuradze"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Successfully returned trainers**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TrainerInfoDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "List of trainers",
                                                    description = "When trainee username exists and there exists an " +
                                                            "array of trainers who are not assigned to specified trainee.",
                                                    value = """
                                                            [
                                                              {
                                                                "username": "Salome.Chachua",
                                                                "firstName": "Salome",
                                                                "lastName": "Chachua",
                                                                "specialization": {
                                                                  "id": 2,
                                                                  "trainingTypeName": "dance"
                                                                }
                                                              },
                                                              {
                                                                "username": "Ilia.Topuria",
                                                                "firstName": "Ilia",
                                                                "lastName": "Topuria",
                                                                "specialization": {
                                                                  "id": 3,
                                                                  "trainingTypeName": "mma"
                                                                }
                                                              },
                                                              {
                                                                "username": "Jack.Sparrow",
                                                                "firstName": "Jacky",
                                                                "lastName": "Sparrow",
                                                                "specialization": {
                                                                  "id": 2,
                                                                  "trainingTypeName": "dance"
                                                                }
                                                              },
                                                              {
                                                                "username": "Jack.Sparrow1",
                                                                "firstName": "Jack",
                                                                "lastName": "Sparrow",
                                                                "specialization": {
                                                                  "id": 2,
                                                                  "trainingTypeName": "dance"
                                                                }
                                                              }
                                                            ]
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Single trainer",
                                                    description = "When every trainer is assigned to specified trainee " +
                                                            "except for one, then the list of single trainer is returned.",
                                                    value = """
                                                            [
                                                              {
                                                                "username": "Ilia.Topuria",
                                                                "firstName": "Ilia",
                                                                "lastName": "Topuria",
                                                                "specialization": {
                                                                  "id": 3,
                                                                  "trainingTypeName": "mma"
                                                                }
                                                              }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Trainee not found with specified username**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Trainee not found",
                                                    description = "No trainee was found in the application with the " +
                                                            "specified username client provided.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainee not found with username: Some.User",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public List<TrainerInfoDto> getTrainersNotAssigned(
            @PathVariable("username") String username
    ) {
        return trainerService.getTrainersNotAssigned(username);
    }

    @PatchMapping("/{username}/active")
    @Operation(
            description = "Endpoint for Activating or Deactivating Trainers",
            summary = "Activate/Deactivate Trainers",
            parameters = @Parameter(
                    name = "username",
                    description = "Username of a Trainer we want to delete",
                    required = true,
                    example = "Merab.Dvalishvili"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActiveStatusDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Valid activation request body",
                                            description = "Valid request body consists of only single field " +
                                                    "isActive which should be either true or false.",
                                            value = """
                                                    {
                                                      "isActive": false
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Not valid activation request body",
                                            description = "If active status is not provided through request " +
                                                    "body then that will cause validation error",
                                            value = """
                                                    {}
                                                    """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updated active status"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Validation error",
                                                    description = "isActive field should not be null, else API " +
                                                            "would be forced to return status code 400 bad request " +
                                                            "and appropriate ErrorDto.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "isActive": "Is Active field should not be null"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Missing or invalid token",
                                                    description = "When bearer token is either missing or invalid there will be an authentication " +
                                                            "error so client will receive response code 401 alongside the following response body.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Token is missing or invalid.",
                                                              "errorCode": "401"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "No permission to access resource",
                                                    description = "When client has a token that is valid but it is for " +
                                                            "user A and tries to access information about user B with the " +
                                                            "same token there will be response code 403 and an error DTO.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Access denied",
                                                              "errorCode": "403"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Trainer not found with provided username",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Username doesn't exist",
                                                    description = "When the provided username is does not exist " +
                                                            "in the database API returns error code 404 with the " +
                                                            "appropriate ErrorDto.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainer not found with username: Jon.Jones",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<Void> activate(
            @PathVariable("username") String username,
            @Valid @RequestBody ActiveStatusDto activeStatusDto
    ) {
        trainerService.updateActiveStatus(username, activeStatusDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/{username}/trainings")
    @Operation(
            description = "Endpoint for filtering trainings based on provided criteria.",
            summary = "Training search",
            parameters = {
                    @Parameter(
                            name = "username",
                            description = "Username of a trainer who's trainings are being searched",
                            required = true,
                            example = "Merab.Dvalishvili"
                    ),
                    @Parameter(
                            name = "from",
                            description = "Starting date **from** what point we are searching",
                            example = "1990-03-07"
                    ),
                    @Parameter(
                            name = "to",
                            description = "End date **to** which point we are searching",
                            example = "2005-11-05"
                    ),
                    @Parameter(
                            name = "traineeUsername",
                            description = "Username of a trainer who conducted the training",
                            example = "Davit.Maisuradze"
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Filters were applied successfully and at least one training was found**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TrainingInfoDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "**Multiple trainings found**",
                                                    description = "When more than one training matched the specified " +
                                                            "filters, then list of training info DTOs are returned.",
                                                    value = """
                                                            [
                                                              {
                                                                "trainingName": "Marathon Prep",
                                                                "trainingDate": "1999-11-05",
                                                                "trainingType": {
                                                                  "id": 1,
                                                                  "trainingTypeName": "box"
                                                                },
                                                                "duration": 90,
                                                                "username": "Davit.Maisuradze"
                                                              },
                                                              {
                                                                "trainingName": "Interval Training",
                                                                "trainingDate": "2024-08-12",
                                                                "trainingType": {
                                                                  "id": 1,
                                                                  "trainingTypeName": "box"
                                                                },
                                                                "duration": 60,
                                                                "username": "John.Doe"
                                                              },
                                                              {
                                                                "trainingName": "boxing",
                                                                "trainingDate": "2024-05-31",
                                                                "trainingType": {
                                                                  "id": 1,
                                                                  "trainingTypeName": "box"
                                                                },
                                                                "duration": 100,
                                                                "username": "John.Doe"
                                                              }
                                                            ]
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "**Single training found**",
                                                    description = "If only one training was found a list of " +
                                                            "single training info DTO is returned.",
                                                    value = """
                                                            [
                                                              {
                                                                "trainingName": "Marathon Prep",
                                                                "trainingDate": "1999-11-05",
                                                                "trainingType": {
                                                                  "id": 1,
                                                                  "trainingTypeName": "box"
                                                                },
                                                                "duration": 90,
                                                                "username": "Davit.Maisuradze"
                                                              }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Validation error**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "**Unsupported date format**",
                                                    description = "When date is provided but in the format that " +
                                                            "is expected validation error occurs.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "expectedFormat": "yyyy-MM-dd",
                                                                "actualValue": "01-01-2004"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Missing or invalid token",
                                                    description = "When bearer token is either missing or invalid there will be an authentication " +
                                                            "error so client will receive response code 401 alongside the following response body.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Token is missing or invalid.",
                                                              "errorCode": "401"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "No permission to access resource",
                                                    description = "When client has a token that is valid but it is for " +
                                                            "user A and tries to access information about user B with the " +
                                                            "same token there will be response code 403 and an error DTO.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Access denied",
                                                              "errorCode": "403"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "When trainee, trainer or a training is not found after applying all the " +
                                    "provided filters error code **404** is the response with response body containing " +
                                    "information about the not found fields.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "**Trainer not found**",
                                                    description = "When the trainer is not found which is the only " +
                                                            "required field API responds with **404** not found error " +
                                                            "code and an *ErrorDto*.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainer not found with username: Jon.Jones",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "**Trainee not found**",
                                                    description = "When the trainee is not found with the provided " +
                                                            "username API responds with 404 not found error " +
                                                            "code and an *ErrorDto*.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainer not found with username: Tom.Cruise",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public List<TrainingInfoDto> getTrainings(
            @PathVariable("username") String username,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "traineeUsername", required = false) String traineeName
    ) {
        TrainerTrainingSearchDto criteria = TrainerTrainingSearchDto.builder()
                .from(from)
                .to(to)
                .name(traineeName)
                .build();
        return trainerService.getTrainingsList(username, criteria);
    }
}