package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.trainingtype.TrainingTypeDto;
import com.davidmaisuradze.gymapplication.entity.TrainingType;
import com.davidmaisuradze.gymapplication.mapper.TrainingTypeMapper;
import com.davidmaisuradze.gymapplication.repository.TrainingTypeRepository;
import com.davidmaisuradze.gymapplication.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTests {
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private TrainingTypeMapper trainingTypeMapper;
    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void testFindAll() {
        when(trainingTypeRepository.findAll()).thenReturn(List.of(new TrainingType(), new TrainingType()));
        when(trainingTypeMapper.entityToDto(any(TrainingType.class))).thenReturn(new TrainingTypeDto());

        List<TrainingTypeDto> dtos = trainingTypeService.findAll();

        assertNotNull(dtos);
        assertFalse(dtos.isEmpty());

        verify(trainingTypeRepository, times(1)).findAll();
    }
}
