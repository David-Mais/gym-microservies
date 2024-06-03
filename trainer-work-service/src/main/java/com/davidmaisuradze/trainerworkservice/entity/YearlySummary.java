package com.davidmaisuradze.trainerworkservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "yearly_summary")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class YearlySummary {
    @Id
    private String id;
    private int year;
    private List<MonthlySummary> monthlySummaries;
}

