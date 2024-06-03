package com.davidmaisuradze.trainerworkservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Month;


@Document(collection = "monthly_summary")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MonthlySummary {
    @Id
    private String id;
    private Month month;
    private int workingMinutes;
}
