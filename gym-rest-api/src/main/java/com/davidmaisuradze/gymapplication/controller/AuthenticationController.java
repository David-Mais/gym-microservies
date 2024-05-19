package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.ErrorDto;
import com.davidmaisuradze.gymapplication.dto.PasswordChangeDto;
import com.davidmaisuradze.gymapplication.dto.security.TokenDto;
import com.davidmaisuradze.gymapplication.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Endpoint for managing Authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
            description = "Endpoint for validating user credentials",
            summary = "Login",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CredentialsDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "User login with valid credentials DTO",
                                        description = "Valid credentials DTO consists of username and" +
                                                " password fields which should be provided and is validated.",
                                        value = """
                                                {
                                                  "username": "John.Doe",
                                                  "password": "pass"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "User login without username",
                                        description = "When username is not provided credentials DTO is not valid " +
                                                "and login will fail.",
                                        value = """
                                                {
                                                  "password": "pass"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "User login without password",
                                        description = "When password is not provided credentials DTO is not valid " +
                                                "and login will fail.",
                                        value = """
                                                {
                                                  "username": "John.Doe"
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
                            description = "**Login Successful**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TokenDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Successful login",
                                                    description = "When credentials match login is successful with" +
                                                            " response code 200 and the following response body.",
                                                    value = """
                                                            {
                                                              "id": 4,
                                                              "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEYXZpdC5NYWlzdXJhZHplMSIsImlhdCI6MTcxNDQ1OTEwNSwiZXhwIjoxNzE0NDc5MTA1fQ.j_TKbE08IPD6BRm5bthmGiBgETKOe9nttP5TyqMUhTk",
                                                              "username": "John.Doe",
                                                              "expiredAt": "2024-04-30T12:11:45.000+00:00"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "**Login Fail**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Failed login",
                                                    description = "When credentials dont match exceptions is being " +
                                                            "thrown and client receives error code 401 and the following " +
                                                            "response body.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Wrong credentials.",
                                                              "errorCode": "401"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<TokenDto> authenticateUser(@RequestBody CredentialsDto credentialsDto) {
        return ResponseEntity.ok(authenticationService.login(credentialsDto));
    }

    @PutMapping("/password/{username}")
    @Operation(
            description = "Updating the password of a user by providing the user whose password client wants " +
                    "to update and also providing PasswordChangeDto containing old password for matching and new " +
                    "password for updating.",
            summary = "Update user password",
            parameters = @Parameter(
                    name = "username",
                    description = "Username of a user whose password client wants to change",
                    required = true,
                    example = "John.Doe"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Correct PasswordChangeDto",
                                            description = "Correct password change DTO should consist of correct old password" +
                                                    " for credentials to match and also new password.",
                                            value = """
                                            {
                                              "oldPassword": "pass",
                                              "newPassword": "newPass"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Incorrect PasswordChangeDto",
                                            description = "Incorrect password change DTO consists of incorrect old password " +
                                                    "and it is not important what the new password is.",
                                            value = """
                                            {
                                              "oldPassword": "incorrectPass",
                                              "newPassword": "newPass"
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
                            description = "**User Password updated successfully**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TokenDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Password update success",
                                                    description = "When password is updated successfully, client gets " +
                                                            "response code 200 and the following token DTO since they " +
                                                            "changed password it means they are authenticated meaning " +
                                                            "it has same permissions as after login",
                                                    value = """
                                                            {
                                                              "id": 2,
                                                              "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTcxNDQ2ODkwOSwiZXhwIjoxNzE0NDg4OTA5fQ.8HK2iG8pRSK5ItGE2yi0nwVWHFd2oKGdyp8EO2TD5e8",
                                                              "username": "John.Doe",
                                                              "expiredAt": "2024-04-30T14:47:56.000+00:00"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "**Unauthorized**",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TokenDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Incorrect old password",
                                                    description = "When incorrect old password is provided authentication " +
                                                            "is not possible so client receives response code 401 and " +
                                                            "the following response body.",
                                                    value = """
                                                            {
                                                              "errorMessage": "Wrong credentials.",
                                                              "errorCode": "401"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<TokenDto> changePassword(
            @PathVariable("username") String username,
            @RequestBody PasswordChangeDto passwordChangeDto
    ) {
        return ResponseEntity.ok(authenticationService.changePassword(username, passwordChangeDto));
    }
}
