package com.davidmaisuradze.trainerworkservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "trainer_workload")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainerWorkSummary {
    @Id
    private String id;
    @Indexed
    private String username;
    @Indexed
    private String firstName;
    @Indexed
    private String lastName;
    private Boolean status;
    private List<YearlySummary> yearlySummaries;
}
