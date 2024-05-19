create table if not exists training_types
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    training_type_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL
);

CREATE TABLE trainees
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_of_birth DATE NOT NULL,
    address       VARCHAR(255),
    FOREIGN KEY (id) REFERENCES users (id)
);

CREATE TABLE trainers
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    specialization       BIGINT,
    FOREIGN KEY (specialization) REFERENCES training_types (id),
    FOREIGN KEY (id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS trainings
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainee_id        BIGINT NOT NULL,
    trainer_id        BIGINT NOT NULL,
    training_name     VARCHAR(255),
    training_type_id  BIGINT NOT NULL,
    training_date     DATE   NOT NULL,
    training_duration INT    NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES trainees (id),
    FOREIGN KEY (trainer_id) REFERENCES trainers (id),
    FOREIGN KEY (training_type_id) REFERENCES training_types (id)
);

CREATE TABLE IF NOT EXISTS tokens
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(300),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);