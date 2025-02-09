package com.davidmaisuradze.gymapplication.repository;

import com.davidmaisuradze.gymapplication.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t INNER JOIN UserEntity u ON t.user.id = u.id WHERE t.user.id = :userId")
    List<Token> findByUserId(@Param("userId") Long userId);

    Optional<Token> findByJwtToken(String token);
}
