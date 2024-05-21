package com.davidmaisuradze.gymapplication.mapper;

import com.davidmaisuradze.gymapplication.dto.training.TrainingInfoDto;
import com.davidmaisuradze.gymapplication.dto.workload.TrainerWorkloadRequest;
import com.davidmaisuradze.gymapplication.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);
    TrainingInfoDto trainingToTrainingInfoDto(Training training);
    @Mapping(source = "trainer.username", target = "username")
    @Mapping(source = "trainer.firstName", target = "firstName")
    @Mapping(source = "trainer.lastName", target = "lastName")
    @Mapping(source = "trainer.isActive", target = "isActive")
    @Mapping(source = "duration", target = "durationMinutes")
    @Mapping(source = "trainingDate", target = "trainingDate")
    TrainerWorkloadRequest trainingToTrainerWorkloadRequest(Training training);
}
