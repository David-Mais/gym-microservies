package com.davidmaisuradze.gymapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication()
@EnableJpaRepositories(basePackages = {"com.davidmaisuradze.gymapplication.repository"})
@EntityScan(basePackages = {"com.davidmaisuradze.gymapplication.entity"})
public class GymApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }
}
