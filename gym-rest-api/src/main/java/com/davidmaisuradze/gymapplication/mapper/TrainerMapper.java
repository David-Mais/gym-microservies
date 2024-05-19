package com.davidmaisuradze.gymapplication.mapper;

import com.davidmaisuradze.gymapplication.dto.trainer.CreateTrainerDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerInfoDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerProfileUpdateResponseDto;
import com.davidmaisuradze.gymapplication.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);
    Trainer createTrainerDtoToTrainer(CreateTrainerDto createTrainerDto);
    TrainerInfoDto trainerToTrainerInfoDto(Trainer trainer);
    TrainerProfileDto trainerToTrainerProfileDto(Trainer trainer);
    TrainerProfileUpdateResponseDto trainerToUpdateResponseDto(Trainer trainer);
}
