INSERT INTO worker (id, name, birthday, level, salary) VALUES
    (1, 'Rostyslav Voloshchak', '2004-03-25', 'Trainee', 800),
    (2, 'Bill Gates', '1985-08-20', 'Senior', 7000),
    (3, 'John Cena', '1982-03-10', 'Middle', 3000),
    (4, 'Ilon Mask', '1995-11-25', 'Senior', 6000),
    (5, 'William Taylor', '1998-09-30', 'Trainee', 900),
    (6, 'Taylor Swift', '1993-07-12', 'Junior', 1500),
    (7, 'Johny Depp', '1987-12-05', 'Middle', 3500),
    (8, 'Robert Martin', '1980-02-28', 'Senior', 7000),
    (9, 'Alexander Garcia', '1991-04-18', 'Trainee', 950),
    (10, 'Martin Odegaard', '1996-06-08', 'Junior', 1300);

INSERT INTO client (id, name) VALUES
    (1, 'Amazon'),
    (2, 'Google'),
    (3, 'Microsoft'),
    (4, 'Apple'),
    (5, 'MSI');

INSERT INTO project (id, client_id, start_date, finish_date) VALUES
    (1, 1, '2023-01-01', '2024-03-31'), 
    (2, 2, '2023-02-15', '2023-05-15'), 
    (3, 3, '2023-03-10', '2023-06-30'), 
    (4, 4, '2023-04-20', '2024-08-31'), 
    (5, 5, '2023-05-05', '2026-07-15'), 
    (6, 1, '2023-06-01', '2023-08-31'), 
    (7, 2, '2020-07-10', '2023-10-31'), 
    (8, 3, '2019-08-15', '2022-11-15'), 
    (9, 4, '2024-01-20', '2027-12-31'), 
    (10, 5, '2023-10-05', '2024-01-05');


INSERT INTO project_worker (project_id, worker_id) VALUES
    (1,3),
    (2,1),
    (1,6),
    (3,7),
    (2,9),
    (5,6),
    (4,4),
    (4,5),
    (3,2),
    (5,10);


