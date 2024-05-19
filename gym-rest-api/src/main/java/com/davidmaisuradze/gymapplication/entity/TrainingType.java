package com.davidmaisuradze.gymapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "training_types")
@Getter
@Setter
@ToString(exclude = {"trainers", "trainings"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "training_type_name", nullable = false, unique = true)
    private String trainingTypeName;

    @OneToMany(mappedBy = "trainingType", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Training> trainings = new HashSet<>();

    @OneToMany(mappedBy = "specialization", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Trainer> trainers = new ArrayList<>();

}
