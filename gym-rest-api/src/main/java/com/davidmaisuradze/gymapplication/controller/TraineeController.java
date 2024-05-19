package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.ErrorDto;
import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.dto.trainee.CreateTraineeDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileUpdateRequestDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileUpdateResponseDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingInfoDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingSearchCriteria;
import com.davidmaisuradze.gymapplication.service.TraineeService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
@Tag(name = "Trainees", description = "Endpoint for managing Trainees")
public class TraineeController {
    private final TraineeService traineeService;

    @PostMapping()
    @Operation(
            description = "Endpoint for registering Trainees with provided details",
            summary = "Register new Trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateTraineeDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Trainee registration with all fields",
                                            description = "All fields that are possible to provide during " +
                                                    "registration, since Username and Password will be generated " +
                                                    "during creation there is no need to provide them here.",
                                            value = """
                                                    {
                                                      "firstName": "Davit",
                                                      "lastName": "Maisuradze",
                                                      "dateOfBirth": "2004-09-20",
                                                      "address": "Georgia"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Trainee registration with minimum fields",
                                            description = "The bare minimum fields that must be provided in order " +
                                                    "for the API to be able to create a trainee, " +
                                                    "address is optional field that can be filled " +
                                                    "out if the trainee would like to do so.",
                                            value = """
                                                    {
                                                      "firstName": "Davit",
                                                      "lastName": "Maisuradze",
                                                      "dateOfBirth": "2004-09-20"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Illegal registration",
                                            description = "This is illegal since without last name it is not +" +
                                                    "possible to create trainee in this API. Problems are the following " +
                                                    "Username cannot be generated and the identification of Trainees would " +
                                                    "be almost impossible",
                                            value = """
                                                    {
                                                      "firstName": "Davit",
                                                      "lastName": "Maisuradze"
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
                            description = "**Trainee registered successfully**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateTraineeDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Trainee registered successfully",
                                                    description = "When trainee is registered successfully " +
                                                            "their newly generated Username and Password are " +
                                                            "provided using CredentialsDto and their Jwt token by " +
                                                            "token DTO.",
                                                    value = """
                                                            {
                                                              "credentials": {
                                                                "username": "Davit.Maisuradze1",
                                                                "password": "eb1f9db3"
                                                              },
                                                              "token": {
                                                                "id": 1,
                                                                "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEYXZpdC5NYWlzdXJhZHplMSIsImlhdCI6MTcxNDQ2OTcxNSwiZXhwIjoxNzE0NDg5NzE1fQ.1p5IBJVJgSJAT2j47vng3Rr9dLnwg-Tni-m0sL2MCME",
                                                                "username": "Davit.Maisuradze1",
                                                                "expiredAt": "2024-04-30T15:08:35.000+00:00"
                                                              }
                                                            }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Trainee registered successfully II",
                                                    description = "When trainee is registered successfully " +
                                                            "their newly generated Username and Password are " +
                                                            "provided using CredentialsDto and their Jwt token by " +
                                                            "token DTO.",
                                                    value = """
                                                            {
                                                              "credentials": {
                                                                "username": "Jane.Deo",
                                                                "password": "4303a9ef"
                                                              },
                                                              "token": {
                                                                "id": 1,
                                                                "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKYW5lLkRlbyIsImlhdCI6MTcxNDQ2OTg3MSwiZXhwIjoxNzE0NDg5ODcxfQ.SmlDNya0-BJl65Fvph3XwJXaVCDtBFG-A3ekNHegH2U",
                                                                "username": "Jane.Deo",
                                                                "expiredAt": "2024-04-30T15:11:11.000+00:00"
                                                              }
                                                            }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Trainee registration error**",
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
                                                    name = "Date of birth not provided",
                                                    description = "When date of birth is not provided it is " +
                                                            "not possible to create Trainee and we return " +
                                                            "status code 400.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "dateOfBirth": "Date of Birth should not be null"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<RegistrationResponse> createTrainee(
            @Valid @RequestBody CreateTraineeDto createTraineeDto
    ) {
        RegistrationResponse registrationResponse = traineeService.create(createTraineeDto);
        return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
    }

    @GetMapping("/profile/{username}")
    @Operation(
            description = "Endpoint for getting Trainee profile by username",
            summary = "Get Trainee profile",
            parameters = @Parameter(
                    name = "username",
                    description = "Trainee username",
                    required = true,
                    example = "Davit.Maisuradze"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Trainee found with provided username**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TraineeProfileDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Existing username provided",
                                                    description = "When username that is provided can be " +
                                                            "found in the database then TraineeProfileDto containing " +
                                                            "all necessary information about that Trainee will be " +
                                                            "returned to user",
                                                    value = """
                                                            {
                                                              "firstName": "Davit",
                                                              "lastName": "Maisuradze",
                                                              "dateOfBirth": "2004-01-01",
                                                              "address": "Kutaisi",
                                                              "isActive": true,
                                                              "trainersList": [
                                                                  {
                                                                      "username": "Merab.Dvalishvili",
                                                                      "firstName": "Merab",
                                                                      "lastName": "Dvlaishvili",
                                                                      "specialization": {
                                                                          "id": 1,
                                                                          "trainingTypeName": "box"
                                                                      }
                                                                  }
                                                              ]
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Existing but not active user",
                                                    description = "When correct username is provided even if the " +
                                                            "trainer is not active profile will still bre returned " +
                                                            "in the same manner active trainer would",
                                                    value = """
                                                            {
                                                              "firstName": "John",
                                                              "lastName": "Doe",
                                                              "dateOfBirth": "1999-12-23",
                                                              "address": "Xashuri",
                                                              "isActive": false,
                                                              "trainersList": [
                                                                {
                                                                  "username": "Merab.Dvalishvili",
                                                                  "firstName": "Merab",
                                                                  "lastName": "Dvlaishvili",
                                                                  "specialization": {
                                                                    "id": 1,
                                                                    "trainingTypeName": "box"
                                                                  }
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
                            responseCode = "404",
                            description = "**Trainee not found with the provided username",
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
                                                              "errorMessage": "Trainee not found with username: Tom.Cruise",
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
                                                                "resourcePath": "api/v1/trainees/profile",
                                                                "detailMessage": "No static resource api/v1/trainees/profile."
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
    public ResponseEntity<TraineeProfileDto> getProfile(
            @PathVariable("username") String username
    ) {
        return ResponseEntity.ok(traineeService.getProfile(username));
    }

    @PutMapping("/profile/{username}")
    @Operation(
            description = "Endpoint used to update details about Trainee profile",
            summary = "Update Trainee profile",
            parameters = @Parameter(
                    name = "username",
                    description = "Username of a Trainee we want to update",
                    required = true,
                    example = "Davit.Maisuradze"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TraineeProfileUpdateRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Valid update request body",
                                            description = "All fields that are possible to provide during " +
                                                    "update request is provided.",
                                            value = """
                                                    {
                                                      "firstName": "Davit",
                                                      "lastName": "Maisuradze",
                                                      "dateOfBirth": "2004-01-01",
                                                      "address": "Georgia",
                                                      "isActive": true
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Not valid update request",
                                            description = "This is not a valid update request body since date of birth " +
                                                    "and is active fields are required when trying to update Trainee " +
                                                    "only field that can be left out is address.",
                                            value = """
                                                    {
                                                      "firstName": "Davit",
                                                      "lastName": "Maisuradze",
                                                      "address": "Georgia",
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
                            description = "**Trainee updated successfully**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TraineeProfileUpdateResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Trainee Profile updates successfully",
                                                    description = "When all the necessary fields are provided " +
                                                            "and the username also exists in the database then " +
                                                            "update succeeds we return status code 200 and the " +
                                                            "response body.",
                                                    value = """
                                                            {
                                                              "username": "Davit.Maisuradze",
                                                              "firstName": "Davit",
                                                              "lastName": "Maisuradze",
                                                              "dateOfBirth": "2004-01-01",
                                                              "address": "Kutai",
                                                              "isActive": true,
                                                              "trainersList": [
                                                                {
                                                                  "username": "Merab.Dvalishvili",
                                                                  "firstName": "Merab",
                                                                  "lastName": "Dvlaishvili",
                                                                  "specialization": {
                                                                    "id": 1,
                                                                    "trainingTypeName": "box"
                                                                  }
                                                                }
                                                              ]
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
                                                                "lastName": "Last name should not be blank",
                                                                "firstName": "First name should not be blank"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "dateOfBirth and isActive not provided",
                                                    description = "When isActive and date of birth fields are not " +
                                                            "provided API is unable to perform an update since it has " +
                                                            "some validation errors, so it marks as 400 bad request.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "dateOfBirth": "must not be null",
                                                                "isActive": "isActive field should not be null"
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
                            description = "Trainee not found with provided username",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Username doesn't exist",
                                                    description = "When the provided username does not exist in the " +
                                                            "database the API is unable to perform an update operation " +
                                                            "since it cannot decide which Trainee to update so it returns " +
                                                            "error code 404 not found as well as ErrorDto containing " +
                                                            "detailed message.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainee not found with username: Tom.Cruise",
                                                              "errorCode": "404"
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
                    )
            }
    )
    public ResponseEntity<TraineeProfileUpdateResponseDto> updateProfile(
            @PathVariable("username") String username,
            @Valid @RequestBody TraineeProfileUpdateRequestDto traineeProfileUpdateRequestDto
    ) {
        return ResponseEntity.ok(traineeService.updateProfile(username, traineeProfileUpdateRequestDto));
    }

    @DeleteMapping("/profile/{username}")
    @Operation(
            description = "Endpoint used for deleting Trainees",
            summary = "Delete Trainee profile",
            parameters = @Parameter(
                    name = "username",
                    description = "Username of a Trainee we want to delete",
                    required = true,
                    example = "Davit.Maisuradze"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Trainee deleted successfully"
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
                            description = "Trainee not found with provided username",
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
                                                              "errorMessage": "Trainee not found with username: Tom.Cruise",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("username") String username) {
        traineeService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{username}/active")
    @Operation(
            description = "Endpoint for Activating or Deactivating Trainees",
            summary = "Activate/Deactivate Trainees",
            parameters = @Parameter(
                    name = "username",
                    description = "Username of a Trainee we want to activate/deactivate",
                    required = true,
                    example = "Davit.Maisuradze"
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
                            description = "Trainee not found with provided username",
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
                                                              "errorMessage": "Trainee not found with username: Tom.Cruise",
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
        traineeService.updateActiveStatus(username, activeStatusDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/{username}/trainings")
    @Operation(
            description = "Endpoint for filtering trainings based on provided criteria.",
            summary = "Training search",
            parameters = {
                    @Parameter(
                            name = "username",
                            description = "Username of a trainee who's trainings are being searched",
                            required = true,
                            example = "Davit.Maisuradze"
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
                            name = "trainerUsername",
                            description = "Username of a trainer who conducted the training",
                            example = "Merab.Dvalishvili"
                    ),
                    @Parameter(
                            name = "trainingTypeName",
                            description = "Type of training that the desired training was",
                            example = "box"
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
                                                    name = "Multiple trainings found",
                                                    description = "When more than one training matched the specified " +
                                                            "filters, then list of training info DTOs are returned.",
                                                    value = """
                                                            [
                                                              {
                                                                "trainingName": "Interval Training",
                                                                "trainingDate": "2024-08-12",
                                                                "trainingType": {
                                                                  "id": 1,
                                                                  "trainingTypeName": "box"
                                                                },
                                                                "duration": 60,
                                                                "username": "Merab.Dvalishvili"
                                                              },
                                                              {
                                                                "trainingName": "boxing session",
                                                                "trainingDate": "2025-03-31",
                                                                "trainingType": {
                                                                  "id": 3,
                                                                  "trainingTypeName": "mma"
                                                                },
                                                                "duration": 100,
                                                                "username": "Ilia.Topuria"
                                                              }
                                                            ]
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Single training found",
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
                                                                "username": "Merab.Dvalishvili"
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
                            description = "When wither training type, trainer or a training is not found after applying all the " +
                                    "provided filters error code **404** is the response with response body containing " +
                                    "information about the not found fields.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "**Trainee not found**",
                                                    description = "When the trainee is not found which is the only " +
                                                            "required field API responds with **404** not found error " +
                                                            "code and an *ErrorDto*.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainee not found with username: Tom.Cruise",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "**Trainer not found**",
                                                    description = "When the trainer is not found with the provided " +
                                                            "username API responds with 404 not found error " +
                                                            "code and an *ErrorDto*.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Trainer not found with username: Jon.Jones",
                                                              "errorCode": "404"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "**Training type not found**",
                                                    description = "If the training type that client provided is not " +
                                                            "available in the database then API responds with 404 not " +
                                                            "found error code and an *ErrorDto*.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Training type not found with name: Acrobatics",
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
            @RequestParam(name = "trainerUsername", required = false) String trainerName,
            @RequestParam(name = "trainingTypeName", required = false) String trainingTypeName
    ) {
        TrainingSearchCriteria criteria = TrainingSearchCriteria.builder()
                .from(from)
                .to(to)
                .name(trainerName)
                .trainingTypeName(trainingTypeName)
                .build();
        return traineeService.getTrainingsList(username, criteria);
    }
}
