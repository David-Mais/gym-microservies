CREATE TABLE IF NOT EXISTS training_types
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    training_type_name VARCHAR(255) NOT NULL UNIQUE
);
ALTER TABLE training_types ALTER COLUMN id RESTART WITH 1;

CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL
);
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;

CREATE TABLE IF NOT EXISTS trainees
(
    id            BIGINT NOT NULL,
    date_of_birth DATE NOT NULL,
    address       VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS trainers
(
    id            BIGINT,
    specialization       BIGINT,
    FOREIGN KEY (specialization) REFERENCES training_types (id),
    PRIMARY KEY (id),
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

DELETE FROM tokens WHERE TRUE;
DELETE FROM trainings WHERE TRUE;
DELETE FROM trainers WHERE TRUE;
DELETE FROM training_types WHERE TRUE;
DELETE FROM trainees WHERE TRUE;
DELETE FROM users WHERE TRUE;

INSERT INTO training_types (training_type_name)
VALUES ('box'), ('dance'), ('mma');

INSERT INTO users (id, first_name, is_active, last_name, password, username)
VALUES
    (1, 'Davit', true, 'Maisuradze', 'newPass', 'Davit.Maisuradze'),
    (2, 'Mariam', true, 'Katamashvili', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Mariam.Katamashvili'),
    (3, 'Merab', true, 'Dvlaishvili', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Merab.Dvalishvili'),
    (4, 'David', true, 'Kheladze', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'David.Kheladze'),
    (5, 'Salome', true, 'Chachua', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Salome.Chachua'),
    (6, 'John', false, 'Doe', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'John.Doe'),
    (7, 'Ilia', true, 'Topuria', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Ilia.Topuria');

INSERT INTO trainees (address, date_of_birth, id)
VALUES
    ('Kutaisi', '2024-09-20', 1),
    ('Tbilisi', '2004-03-01', 2),
    ('Batumi', '2000-06-17', 4),
    ('Xashuri', '1999-12-23', 6);

INSERT INTO trainers (specialization, id)
VALUES
    (1, 3),
    (2, 5),
    (3, 7);

INSERT INTO trainings (training_duration, training_date, training_name, trainee_id, trainer_id, training_type_id)
VALUES
    (90, '1999-11-05', 'Marathon Prep', 1, 3, 1),
    (60, '2010-12-15', 'Yoga Basics', 2, 5, 2),
    (75, '2020-04-10', 'Weightlifting 101', 4, 7, 3),
    (60, '2024-08-12', 'Interval Training', 6, 3, 1);