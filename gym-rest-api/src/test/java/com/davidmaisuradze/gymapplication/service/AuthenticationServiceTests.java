package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.PasswordChangeDto;
import com.davidmaisuradze.gymapplication.dto.security.TokenDto;
import com.davidmaisuradze.gymapplication.entity.Token;
import com.davidmaisuradze.gymapplication.entity.Trainee;
import com.davidmaisuradze.gymapplication.entity.UserEntity;
import com.davidmaisuradze.gymapplication.exception.GymException;
import com.davidmaisuradze.gymapplication.repository.TokenRepository;
import com.davidmaisuradze.gymapplication.repository.UserRepository;
import com.davidmaisuradze.gymapplication.security.JwtTokenProvider;
import com.davidmaisuradze.gymapplication.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testLogin_WhenCredentialsAreValid_ThenReturnTokenDto() {
        CredentialsDto credentialsDto = new CredentialsDto("validUser", "validPass");
        UserEntity userEntity = new Trainee();
        userEntity.setId(1L);
        userEntity.setUsername("validUser");

        Token token = new Token();

        when(authenticationManager.authenticate(any())).thenReturn(null); // Mock successful authentication
        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(userEntity));
        when(jwtTokenProvider.generateToken(any())).thenReturn("mockToken");
        when(jwtTokenProvider.extractUsername("mockToken")).thenReturn("validUser");
        when(jwtTokenProvider.getExpiration("mockToken")).thenReturn(new Date(System.currentTimeMillis() + 20000000));
        when(tokenRepository.findByUserId(1L)).thenReturn(List.of(token));
        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        TokenDto tokenDto = authenticationService.login(credentialsDto);

        assertNotNull(tokenDto);
        assertEquals("mockToken", tokenDto.getToken());
        assertEquals("validUser", tokenDto.getUsername());

        verify(tokenRepository, times(1)).findByUserId(1L);
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testLogin_WhenCredentialsAreInvalid_ThenReturnTokenDto() {
        CredentialsDto credentialsDto = new CredentialsDto("invalidUser", "invalidPass");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        GymException exception = assertThrows(
                GymException.class,
                () -> authenticationService.login(credentialsDto)
        );

        assertEquals("Wrong credentials.", exception.getMessage());
        assertEquals("401", exception.getErrorCode());

        verify(loginAttemptService).loginFailed(anyString());
    }

    @Test
    @Transactional
    void testChangePassword_WhenCredentialsAreValid_ThenReturnTokenDto() {
        String username = "validUser";
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("oldPass", "newPass");
        UserEntity userEntity = new Trainee();
        userEntity.setId(1L);
        userEntity.setUsername("validUser");
        userEntity.setPassword("oldPass");

        Token token = new Token();
        token.setId(1L);
        token.setJwtToken("mockToken");

        when(loginAttemptService.isLockedOut(anyString())).thenReturn(false);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(jwtTokenProvider.generateToken(any())).thenReturn(token.getJwtToken());
        when(jwtTokenProvider.extractUsername("mockToken")).thenReturn("validUser");
        when(jwtTokenProvider.getExpiration("mockToken")).thenReturn(new Date(System.currentTimeMillis() + 20000000));
        when(tokenRepository.findByUserId(1L)).thenReturn(List.of(token));
        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        TokenDto tokenDto = authenticationService.changePassword(username, passwordChangeDto);

        assertNotNull(tokenDto);
        assertEquals(token.getJwtToken(), tokenDto.getToken());
        assertEquals("validUser", tokenDto.getUsername());

        verify(loginAttemptService, times(1)).isLockedOut(anyString());
        verify(tokenRepository, times(1)).save(any(Token.class));
        verify(tokenRepository, times(1)).findByUserId(1L);
    }

    @Test
    @Transactional
    void testChangePassword_WhenCredentialsAreInvalid_ThenThrowGymException() {
        String username = "invalidUser";
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("oldPass", "newPass");
        UserEntity userEntity = new Trainee();
        userEntity.setId(1L);
        userEntity.setUsername("invalidUser");
        userEntity.setPassword("oldPass");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        GymException exception = assertThrows(
                GymException.class,
                () -> authenticationService.changePassword(username, passwordChangeDto)
        );
        assertEquals("Wrong credentials.", exception.getMessage());
        assertEquals("401", exception.getErrorCode());
        verify(loginAttemptService, times(1)).isLockedOut(anyString());
    }
}
