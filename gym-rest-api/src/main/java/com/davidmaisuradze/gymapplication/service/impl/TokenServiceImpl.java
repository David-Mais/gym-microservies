package com.davidmaisuradze.gymapplication.service.impl;

import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.dto.security.TokenDto;
import com.davidmaisuradze.gymapplication.entity.Token;
import com.davidmaisuradze.gymapplication.repository.TokenRepository;
import com.davidmaisuradze.gymapplication.security.GymUserDetails;
import com.davidmaisuradze.gymapplication.security.JwtTokenProvider;
import com.davidmaisuradze.gymapplication.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RegistrationResponse register(GymUserDetails user, String username, String password) {
        String token = jwtTokenProvider.generateToken(user);

        Token tokenEntity = Token
                .builder()
                .jwtToken(token)
                .user(user.getUserEntity())
                .build();
        tokenEntity = tokenRepository.save(tokenEntity);

        return new RegistrationResponse(
                new CredentialsDto(username, password),
                new TokenDto(
                        tokenEntity.getId(),
                        token,
                        jwtTokenProvider.extractUsername(token),
                        jwtTokenProvider.getExpiration(token)
                )
        );
    }
}
