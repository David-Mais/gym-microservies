package com.davidmaisuradze.gymapplication.service.impl;

import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.PasswordChangeDto;
import com.davidmaisuradze.gymapplication.dto.security.TokenDto;
import com.davidmaisuradze.gymapplication.entity.Token;
import com.davidmaisuradze.gymapplication.entity.UserEntity;
import com.davidmaisuradze.gymapplication.exception.GymException;
import com.davidmaisuradze.gymapplication.repository.TokenRepository;
import com.davidmaisuradze.gymapplication.repository.UserRepository;
import com.davidmaisuradze.gymapplication.security.GymUserDetails;
import com.davidmaisuradze.gymapplication.security.JwtTokenProvider;
import com.davidmaisuradze.gymapplication.service.AuthenticationService;
import com.davidmaisuradze.gymapplication.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public TokenDto login(CredentialsDto credentialsDto) {
        String username = credentialsDto.getUsername();

        checkLock(username);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            credentialsDto.getPassword()
                    )
            );
            loginAttemptService.loginSucceeded(username);
        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(username);
            throw new GymException("Wrong credentials.", "401");
        }

        UserEntity userEntity = userRepository
                .findByUsername(credentialsDto.getUsername())
                .orElseThrow(() -> new GymException("User not found", "404"));

        return generateTokenDto(userEntity);
    }

    @Override
    @Transactional
    public TokenDto
    changePassword(String username, PasswordChangeDto passwordChangeDto) {
        checkLock(username);

        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new GymException("User not found", "404"));

        if (!passwordEncoder.matches(passwordChangeDto.getOldPassword(), userEntity.getPassword())) {
            loginAttemptService.loginFailed(username);
            throw new GymException("Wrong credentials.", "401");
        }

        loginAttemptService.loginSucceeded(username);

        userEntity.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        userRepository.save(userEntity);

        return generateTokenDto(userEntity);
    }


    private TokenDto generateTokenDto(UserEntity userEntity) {
        GymUserDetails user = new GymUserDetails(userEntity);

        String token = jwtTokenProvider.generateToken(user);

        List<Token> tokens = tokenRepository.findByUserId(userEntity.getId());
        if (!tokens.isEmpty()) {
            tokenRepository.deleteAll(tokens);
        }
        Token tokenEntity = Token
                .builder()
                .user(userEntity)
                .jwtToken(token)
                .build();
        tokenEntity = tokenRepository.save(tokenEntity);

        return TokenDto.builder()
                .id(tokenEntity.getId())
                .token(token)
                .username(jwtTokenProvider.extractUsername(token))
                .expiredAt(jwtTokenProvider.getExpiration(token))
                .build();
    }

    private void checkLock(String username) {
        if (loginAttemptService.isLockedOut(username)) {
            throw new GymException("Account is locked. Try again later.", "401");
        }
    }
}
