package com.davidmaisuradze.gymapplication.repository;

import com.davidmaisuradze.gymapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query("select u.password from UserEntity u where u.username = :username")
    Optional<String> findPasswordByUsername(@Param("username") String username);

    @Query("select u.username from UserEntity u")
    List<String> findAllUsernames();
}
