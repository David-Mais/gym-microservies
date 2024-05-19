package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.ErrorDto;
import com.davidmaisuradze.gymapplication.exception.GymException;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Order()
public class ErrorController {

    @ExceptionHandler(GymException.class)
    public ResponseEntity<ErrorDto> handleCreationException(GymException e) {
        ErrorDto dto = new ErrorDto();
        dto.setErrorCode(e.getErrorCode());
        dto.setErrorMessage(e.getMessage());
        HttpStatus status = codeToStatus(dto.getErrorCode());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationError(MethodArgumentNotValidException e) {
        ErrorDto errorDto = new ErrorDto();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errorDto.getDetails().put(fieldError.getField(), fieldError.getDefaultMessage()));

        errorDto.setErrorMessage("Validation error");
        errorDto.setErrorCode("400");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(NoResourceFoundException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorCode("404");
        errorDto.setErrorMessage("Resource not found");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("resourcePath", e.getResourcePath());
        errorDetails.put("detailMessage", e.getMessage());
        errorDto.setDetails(errorDetails);
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorDto> handleJsonParseException(JsonParseException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorCode("400");
        errorDto.setErrorMessage("Error parsing JSON");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", e.getMessage());
        errorDto.setDetails(errorDetails);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleDateTimeParseException(MethodArgumentTypeMismatchException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorCode("400");
        errorDto.setErrorMessage("Validation error");

        Map<String, Object> errorDetails = new HashMap<>();
        if (Objects.requireNonNull(e.getRequiredType()).toString().contains("LocalDate")) {
            errorDetails.put("expectedFormat", "yyyy-MM-dd");
        }
        errorDetails.put("actualValue", e.getValue());
        errorDto.setDetails(errorDetails);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }



    private HttpStatus codeToStatus(String errorCode) {
        try {
            int status = Integer.parseInt(errorCode);
            return HttpStatus.resolve(status) != null ?
                    HttpStatus.resolve(status) :
                    HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
