package com.davidmaisuradze.gymapplication.repository;

import com.davidmaisuradze.gymapplication.entity.Token;
import com.davidmaisuradze.gymapplication.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = "/database/test-schema.sql")
@ActiveProfiles("test")
class TokenRepositoryTests {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserId_WhnUserExists_ThenReturnListOfTokens() {
        String jwt = "token123";
        UserEntity user = userRepository.findById(1L).orElse(null);
        Token token = Token
                .builder()
                .jwtToken(jwt)
                .user(user)
                .build();
        tokenRepository.save(token);

        assert user != null;
        List<Token> tokens = tokenRepository.findByUserId(user.getId());
        assertThat(tokens).isNotEmpty().hasSize(1);
        assertThat(tokens.get(0).getJwtToken()).isEqualTo(jwt);
    }

    @Test
    void findByUserId_WhenUserNotExists_ThenReturnEmptyList() {
        Long userId = 99L;
        List<Token> tokens = tokenRepository.findByUserId(userId);

        assertThat(tokens).isEmpty();
    }

    @Test
    void findTokenByJwt_WhenTokenExists_ThenReturnToken() {
        String jwt = "token123";
        UserEntity user = userRepository.findById(1L).orElse(null);
        Token token = Token
                .builder()
                .jwtToken(jwt)
                .user(user)
                .build();
        tokenRepository.save(token);

        Optional<Token> actualToken = tokenRepository.findByJwtToken(jwt);

        assertThat(actualToken).isPresent();
        assertThat(actualToken.get().getJwtToken()).isEqualTo(jwt);
    }

    @Test
    void findTokenByJwt_WhenTokenNotExists_ThenReturnEmptyList() {
        String jwt = "token123";

        Optional<Token> actualToken = tokenRepository.findByJwtToken(jwt);

        assertThat(actualToken).isEmpty();
    }
}
