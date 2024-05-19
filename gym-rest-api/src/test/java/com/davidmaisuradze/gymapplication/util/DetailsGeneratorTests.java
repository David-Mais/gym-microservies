package com.davidmaisuradze.gymapplication.util;

import com.davidmaisuradze.gymapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class DetailsGeneratorTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DetailsGenerator detailsGenerator;

    @Test
    void testGeneratePassword_ThenReturnRandomPassword() {
        String password = detailsGenerator.generatePassword();
        assertThat(password).isNotNull().hasSize(8);
    }

    @Test
    void testGenerateUsername_WhenUsernameIsNew_ThenReturnCorrectUsername() {
        String first = "First";
        String last = "Last";

        String username = detailsGenerator.generateUsername(first, last);
        assertThat(username).isNotNull().isEqualTo("First.Last");
    }

    @Test
    void testGenerateUsername_WhenUsernameAlreadyExists_ThenReturnCorrectUsername() {
        when(userRepository.findAllUsernames()).thenReturn(List.of("Davit.Maisuradze"));

        String first = "Davit";
        String last = "Maisuradze";

        String username = detailsGenerator.generateUsername(first, last);
        assertThat(username).isNotNull().isEqualTo("Davit.Maisuradze1");
    }
}
