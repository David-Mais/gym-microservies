package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.ErrorDto;
import com.davidmaisuradze.gymapplication.dto.training.CreateTrainingDto;
import com.davidmaisuradze.gymapplication.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
@Tag(name = "Trainings", description = "Endpoint for managing Trainings")
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping()
    @Operation(
            description = "Endpoint for creating trainings by providing all necessary information",
            summary = "Create training",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateTrainingDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Training creation valid",
                                            description = "All the fields in CreateTrainingDto are provided",
                                            value = """
                                                    {
                                                      "traineeUsername": "John.Doe",
                                                      "trainerUsername": "Merab.Dvalishvili",
                                                      "trainingName": "boxing",
                                                      "trainingDate": "2024-05-31",
                                                      "duration": 100
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Training creation invalid",
                                            description = "Not sufficient information for creating training. Trainer " +
                                                    "username and training date are not provided.",
                                            value = """
                                                    {
                                                      "traineeUsername": "John.Doe",
                                                      "trainingName": "boxing",
                                                      "duration": 100
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
                            description = "**Training created successfullt**"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Training creation error. Reason: Validation.**",
                            content = @Content(
                                    mediaType = "application.json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "No Trainee Trainer usernames",
                                                    description = "When no trainer and/or trainee usernames are " +
                                                            "provided it is impossible for the API to create training " +
                                                            "so it returns status code 400 bad request and an error " +
                                                            "DTO containing error message.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "trainerUsername": "Trainer username should not be blank",
                                                                "traineeUsername": "Trainee username should not be blank"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "No Training name",
                                                    description = "It is required to specify training name when " +
                                                            "creating training",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "trainingName": "Training name should not be blank"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "No Training date",
                                                    description = "It is required to specify training date when " +
                                                            "creating training",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "trainingName": "Training date should not be blank"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "No Training duration",
                                                    description = "It is required to specify training duration when " +
                                                            "creating training",
                                                    value = """
                                                            {
                                                              "errorMessage": "Validation error",
                                                              "details": {
                                                                "trainingName": "Training duration should not be blank"
                                                              },
                                                              "errorCode": "400"
                                                            }
                                                            """
                                            ),
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
            }
    )
    public ResponseEntity<Void> create(
            @Valid @RequestBody CreateTrainingDto createTrainingDto
    ) {
        trainingService.create(createTrainingDto);
        return ResponseEntity.ok().build();
    }
}