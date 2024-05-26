package com.davidmaisuradze.trainerworkservice.controller;

import com.davidmaisuradze.trainerworkservice.dto.WorkSummaryDto;
import com.davidmaisuradze.trainerworkservice.service.TrainerWorkSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainerWorkSummaryService trainerService;

    @GetMapping
    public ResponseEntity<List<WorkSummaryDto>> all() {
        return ResponseEntity.ok(trainerService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<WorkSummaryDto> workSummary(@PathVariable("username") String username) {
        return ResponseEntity.ok(trainerService.findByUsername(username));
    }
}
