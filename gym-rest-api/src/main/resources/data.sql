INSERT INTO users (first_name, is_active, last_name, password, username)
VALUES
    ('Davit', 1, 'Maisuradze', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Davit.Maisuradze'),
    ('Mariam', 1, 'Katamashvili', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Mariam.Katamashvili'),
    ('Merab', 1, 'Dvlaishvili', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Merab.Dvalishvili'),
    ('David', 1, 'Kheladze', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'David.Kheladze'),
    ('Salome', 1, 'Chachua', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Salome.Chachua'),
    ('John', 0, 'Doe', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'John.Doe'),
    ('Ilia', 1, 'Topuria', '$2a$12$9leKhVdZ6r5i/79AcrlNF.ppbHk3m0Ef.PgHddUGa1a/AuuPE1V9i', 'Ilia.Topuria');

INSERT INTO training_types (training_type_name)
VALUES ('box'), ('dance'), ('mma');

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
