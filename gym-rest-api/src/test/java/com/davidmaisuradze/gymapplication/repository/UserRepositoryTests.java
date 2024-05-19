package com.davidmaisuradze.gymapplication.repository;

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
class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername_WhenUserExists_ThenReturnUser() {
        String username = "Davit.Maisuradze";
        Optional<UserEntity> user = userRepository.findByUsername(username);
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(username);
    }

    @Test
    void testFindByUsername_WhenUserDoesNotExist_ThenReturnNull() {
        String username = "Non.Existing";
        Optional<UserEntity> user = userRepository.findByUsername(username);
        assertThat(user).isEmpty();
    }

    @Test
    void testFindPasswordByUsername_WhenUserExists_ThenReturnUserPassword() {
        String username = "Davit.Maisuradze";
        String password = "newPass";
        Optional<String> actualPassOptional = userRepository.findPasswordByUsername(username);
        assertThat(actualPassOptional).isPresent();

        String actualPass = actualPassOptional.get();
        assertThat(actualPass).isEqualTo(password);
    }

    @Test
    void testFindPasswordByUsername_WhenUserDoesNotExist_ThenReturnEmpty() {
        String username = "Non.Existing";

        Optional<String> actualPassOptional = userRepository.findPasswordByUsername(username);

        assertThat(actualPassOptional).isEmpty();
    }

    @Test
    void testFindAllUsernames_ThenReturnAllUsernames() {
        List<String> usernames = userRepository.findAllUsernames();
        assertThat(usernames).isNotNull().hasSize(7);
    }
}
