-- Inicialización de USERS
INSERT INTO USERS (id, username, password)
VALUES (1, 'admin', '$2a$12$2TY0tg4iQBgTLDv8rhyReOS/lWg9pP8Aig88s0Q3jLaSO4JwUcLNy'); -- password: admin123
INSERT INTO USERS (id, username, password)
VALUES (2, 'doctor1', '$2a$12$msRtpjySy3yDAmYsZ6J7a.Oggs1sxuOjN/UHrHtkTINdK9VcexDaW'); -- password: doctor123
INSERT INTO USERS (id, username, password)
VALUES (3, 'doctor2', '$2a$12$msRtpjySy3yDAmYsZ6J7a.Oggs1sxuOjN/UHrHtkTINdK9VcexDaW'); -- password: doctor123
INSERT INTO USERS (id, username, password)
VALUES (4, 'patient1', '$2a$12$kJbCtGPdd1SBYQVcuafQseS3HEuQPWyh1MjXBqRKjd0csmUfydeXi'); -- password: patient123
INSERT INTO USERS (id, username, password)
VALUES (5, 'patient2', '$2a$12$kJbCtGPdd1SBYQVcuafQseS3HEuQPWyh1MjXBqRKjd0csmUfydeXi'); -- password: patient123
INSERT INTO USERS (id, username, password)
VALUES (6, 'admin2', '$2a$12$YSNziUwWGVXEEpMkDZBC0eNjQBMiqVn/BBAac./Q1AD8..FBuhI1y'); -- password: admin2
INSERT INTO USERS (id, username, password)
VALUES (7, 'doctor3', '$2a$12$pzLfcJuQMsglbeJs1DiZIu0jdBILaQ1paMyAlre8X3j5UEH946jcW'); -- password: doctor123
INSERT INTO USERS (id, username, password)
VALUES (8, 'labtech1', '$2a$12$qNFiU2ZY7gPhOjGZNFqgsuMZw/YKIf9BKvKE8RIJiHE.xfV2f7d86'); -- password: lab123

INSERT INTO USERS (id, username, password)
VALUES (9, 'administrative1', '$2a$12$Ne0e0Y2V7CPWEVOJ6Y3fIOkPsmWj56AQtx/vO1JBcPVCDqQaomwGO');
-- password: administrative123


-- Inicialización de ROLES
INSERT INTO ROLES (id, name)
VALUES (1, 'ROLE_ADMIN');
INSERT INTO ROLES (id, name)
VALUES (2, 'ROLE_DOCTOR');
INSERT INTO ROLES (id, name)
VALUES (3, 'ROLE_PATIENT');
INSERT INTO ROLES (id, name)
VALUES (4, 'ROLE_ADMINISTRATIVE');
INSERT INTO ROLES (id, name)
VALUES (5, 'ROLE_LAB_TECHNICIAN');


-- Inicialización de USER_ROLES
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (1, 1);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (2, 2);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (3, 2);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (4, 3);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (5, 3);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (6, 1);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (7, 2);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (8, 5); -- labtech1 -> LAB_TECHNICIAN
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (9, 4);
-- administrative1 -> ADMINITRATIVE


-- Inicialización de PERMISSIONS
INSERT INTO PERMISSIONS (id, name)
VALUES (1, 'READ_ALL');
INSERT INTO PERMISSIONS (id, name)
VALUES (2, 'WRITE_ALL');
INSERT INTO PERMISSIONS (id, name)
VALUES (3, 'READ_PATIENTS');
INSERT INTO PERMISSIONS (id, name)
VALUES (4, 'WRITE_PATIENTS');
INSERT INTO PERMISSIONS (id, name)
VALUES (5, 'READ_APPOINTMENTS');
INSERT INTO PERMISSIONS (id, name)
VALUES (6, 'WRITE_APPOINTMENTS');
INSERT INTO PERMISSIONS (id, name)
VALUES (7, 'READ_TREATMENTS');
INSERT INTO PERMISSIONS (id, name)
VALUES (8, 'WRITE_TREATMENTS');
INSERT INTO PERMISSIONS (id, name)
VALUES (9, 'READ_EXAMINATIONS');
INSERT INTO PERMISSIONS (id, name)
VALUES (10, 'WRITE_EXAMINATIONS');
INSERT INTO PERMISSIONS (id, name)
VALUES (11, 'READ_OWN_DATA');

-- Inicialización de ROLE_PERMISSIONS
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (1, 1);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (1, 2);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (2, 3);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (2, 4);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (2, 5);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (2, 6);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (2, 7);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (2, 8);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (2, 9);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (2, 10);
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id)
VALUES (3, 11);

-- Inicialización de PERSONALS
INSERT INTO PERSONAL (id, id_number, name, last_name, email, phone_number, date_of_hiring, status, user_id)
VALUES (1, 'ADM001', 'Juan', 'Pérez', 'juan.perez@hospital.com', '555-1234', TIMESTAMP '2022-01-15 00:00:00', 'A', 1);

INSERT INTO PERSONAL (id, id_number, name, last_name, email, phone_number, date_of_hiring, status, user_id)
VALUES (2, 'DOC001', 'María', 'González', 'maria.gonzalez@hospital.com', '555-2345', TIMESTAMP '2022-02-01 00:00:00',
        'A', 2);

INSERT INTO PERSONAL (id, id_number, name, last_name, email, phone_number, date_of_hiring, status, user_id)
VALUES (3, 'NUR001', 'Carlos', 'Rodríguez', 'carlos.rodriguez@hospital.com', '555-3456',
        TIMESTAMP '2022-03-15 00:00:00', 'A', 3);

INSERT INTO PERSONAL (id, id_number, name, last_name, email, phone_number, date_of_hiring, status, user_id)
VALUES (4, 'ADM002', 'Laura', 'Martínez', 'laura.martinez@hospital.com', '555-4567', TIMESTAMP '2022-04-01 00:00:00',
        'A', 6);

INSERT INTO PERSONAL (id, id_number, name, last_name, email, phone_number, date_of_hiring, status, user_id)
VALUES (5, 'DOC002', 'Pedro', 'Sánchez', 'pedro.sanchez@hospital.com', '555-5678', TIMESTAMP '2022-05-15 00:00:00', 'A',
        7);
INSERT INTO PERSONAL (id, id_number, name, last_name, email, phone_number, date_of_hiring, status, user_id)
VALUES (6, 'LAB001', 'Andrés', 'Mejía', 'andres.mejia@hospital.com', '555-8901', TIMESTAMP '2022-06-01 00:00:00', 'A',
        8);

INSERT INTO PERSONAL (id, id_number, name, last_name, email, phone_number, date_of_hiring, status, user_id)
VALUES (7, 'ADM002', 'Elena', 'Morales', 'elena.morales@hospital.com', '555-9012', TIMESTAMP '2022-06-15 00:00:00', 'A',
        9);


-- Inicialización de DOCTORS
INSERT INTO DOCTORS (id, medical_license_number)
VALUES (2, 'MED12345');
INSERT INTO DOCTORS (id, medical_license_number)
VALUES (5, 'MED54321');

-- Inicialización de ADMINISTRATIVE
INSERT INTO ADMINISTRATIVE (id, position, department)
VALUES (1, 'Coordinador', 'Administración');
INSERT INTO ADMINISTRATIVE (id, position, department)
VALUES (4, 'Asistente', 'Recursos Humanos');

-- Inicialización de PATIENTS
INSERT INTO PATIENTS (id, user_id, name, birthdate, gender, address, phone_number, email)
VALUES (1, 4, 'Ana López', TIMESTAMP '1985-05-10 00:00:00', 'F', 'Calle Principal 123', '555-6789',
        'ana.lopez@email.com');

INSERT INTO PATIENTS (id, user_id, name, birthdate, gender, address, phone_number, email)
VALUES (2, 5, 'Roberto Fernández', TIMESTAMP '1978-08-15 00:00:00', 'M', 'Avenida Central 456', '555-7890',
        'roberto.fernandez@email.com');

-- Inicialización de MEDICAL_HISTORIES
INSERT INTO MEDICAL_HISTORIES (id, patient_id, creation_date, current_health_status)
VALUES (1, 1, TIMESTAMP '2023-01-01 00:00:00', 'Saludable');
INSERT INTO MEDICAL_HISTORIES (id, patient_id, creation_date, current_health_status)
VALUES (2, 2, TIMESTAMP '2023-01-02 00:00:00', 'Tratamiento en curso');

-- Inicialización de AVAILABILITIES
INSERT INTO AVAILABILITIES (id, start_time, end_time)
VALUES (1, TIMESTAMP '2023-03-01 08:00:00', TIMESTAMP '2023-03-01 16:00:00');
INSERT INTO AVAILABILITIES (id, start_time, end_time)
VALUES (2, TIMESTAMP '2023-03-02 08:00:00', TIMESTAMP '2023-03-02 16:00:00');
INSERT INTO AVAILABILITIES (id, start_time, end_time)
VALUES (3, TIMESTAMP '2023-03-03 08:00:00', TIMESTAMP '2023-03-03 16:00:00');

-- Inicialización de PERSONAL_AVAILABILITIES
INSERT INTO PERSONAL_AVAILABILITIES (personal_id, availability_id)
VALUES (2, 1);
INSERT INTO PERSONAL_AVAILABILITIES (personal_id, availability_id)
VALUES (2, 2);
INSERT INTO PERSONAL_AVAILABILITIES (personal_id, availability_id)
VALUES (5, 3);

-- Inicialización de STATUSES
INSERT INTO STATUSES (id, name, availability_id)
VALUES (1, 'Disponible', 1);
INSERT INTO STATUSES (id, name, availability_id)
VALUES (2, 'Ocupado', 2);
INSERT INTO STATUSES (id, name, availability_id)
VALUES (3, 'Disponible', 3);

-- Inicialización de TYPE_OF_MEDICAL_APPOINTMENTS
INSERT INTO TYPE_OF_MEDICAL_APPOINTMENTS (id, name)
VALUES (1, 'Consulta general');
INSERT INTO TYPE_OF_MEDICAL_APPOINTMENTS (id, name)
VALUES (2, 'Revisión de rutina');
INSERT INTO TYPE_OF_MEDICAL_APPOINTMENTS (id, name)
VALUES (3, 'Consulta especializada');

-- Inicialización de APPOINTMENT_RESULTS
INSERT INTO APPOINTMENT_RESULTS (id, evaluation_date, medical_history_id)
VALUES (1, TIMESTAMP '2023-03-01 09:30:00', 1);
INSERT INTO APPOINTMENT_RESULTS (id, evaluation_date, medical_history_id)
VALUES (2, TIMESTAMP '2023-03-02 10:45:00', 2);

-- Inicialización de OBSERVATIONS
INSERT INTO OBSERVATIONS (id, content, recommendation, appointment_result_id)
VALUES (1, 'Paciente presenta síntomas de resfriado común', 'Reposo y abundante líquido', 1);
INSERT INTO OBSERVATIONS (id, content, recommendation, appointment_result_id)
VALUES (2, 'Control de presión arterial', 'Continuar con medicación actual', 2);

-- Inicialización de TREATMENTS
INSERT INTO TREATMENTS (id, name, description, date_start, end_date, appointment_result_id)
VALUES (1, 'Tratamiento para resfriado', 'Medicamentos para aliviar síntomas', TIMESTAMP '2023-03-01 10:00:00',
        TIMESTAMP '2023-03-08 23:59:59', 1);
INSERT INTO TREATMENTS (id, name, description, date_start, end_date, appointment_result_id)
VALUES (2, 'Control de hipertensión', 'Seguimiento de presión arterial', TIMESTAMP '2023-03-02 11:00:00',
        TIMESTAMP '2023-04-02 23:59:59', 2);

-- Inicialización de MEDICAL_APPOINTMENTS
INSERT INTO MEDICAL_APPOINTMENTS (id, doctor_id, type_of_medical_appointment_id, appointment_date, id3, treatment_id,
                                  medical_history_id)
VALUES (1, 2, 1, TIMESTAMP '2023-03-01 09:00:00', 1, 1, 1);
INSERT INTO MEDICAL_APPOINTMENTS (id, doctor_id, type_of_medical_appointment_id, appointment_date, id3, treatment_id,
                                  medical_history_id)
VALUES (2, 5, 2, TIMESTAMP '2023-03-02 10:00:00', 2, 2, 2);

-- Inicialización de MEDICAL_OFFICES
INSERT INTO MEDICAL_OFFICES (id, medical_appointment_id, name)
VALUES (1, 1, 'Consultorio 101');
INSERT INTO MEDICAL_OFFICES (id, medical_appointment_id, name)
VALUES (2, 2, 'Consultorio 202');

-- Inicialización de LABORATORIES
INSERT INTO LABORATORIES (id, name, location, telephone)
VALUES (1, 'Lab Central', 'Edificio A, Planta 2', '555-8901');
INSERT INTO LABORATORIES (id, name, location, telephone)
VALUES (2, 'Lab Especializado', 'Edificio B, Planta 1', '555-9012');

-- Inicialización de TYPE_OF_EXAMS
INSERT INTO TYPE_OF_EXAMS (id, name, description)
VALUES (1, 'Análisis de sangre', 'Hemograma completo');
INSERT INTO TYPE_OF_EXAMS (id, name, description)
VALUES (2, 'Radiografía', 'Imagen de tórax');

-- Inicialización de MEDICAL_EXAMINATIONS
INSERT INTO MEDICAL_EXAMINATIONS (id, date_of_realization, laboratory_id, type_of_exam_id, medical_history_id)
VALUES ('EX0001', TIMESTAMP '2023-03-03 11:00:00', 1, 1, 1);
INSERT INTO MEDICAL_EXAMINATIONS (id, date_of_realization, laboratory_id, type_of_exam_id, medical_history_id)
VALUES ('EX0002', TIMESTAMP '2023-03-04 14:30:00', 2, 2, 2);

-- Inicialización de EXAMINATION_RESULTS
-- Nota: El campo BLOB se inserta como una cadena vacía, ajustar según sea necesario
INSERT INTO EXAMINATION_RESULTS (id, generation_date, results_report, medical_history_id)
VALUES (1, TIMESTAMP '2023-03-03 15:00:00', pg_read_binary_file('/docker-entrypoint-init-db.d/files/example-1.jpg'), 1);
INSERT INTO EXAMINATION_RESULTS (id, generation_date, results_report, medical_history_id)
VALUES (2, TIMESTAMP '2023-03-04 17:00:00', pg_read_binary_file('/docker-entrypoint-init-db.d/files/example-2.png'), 2);

-- Inicialización de PRESCRIBED_MEDICINES
INSERT INTO PRESCRIBED_MEDICINES (id, medicine, prescription_date, instructions, dose, duration,
                                  frequency_of_administration, treatment_id)
VALUES (1, 'Paracetamol', TIMESTAMP '2023-03-01 10:00:00', 'Tomar con las comidas', '500mg', 7, 'Cada 8 horas', 1);
INSERT INTO PRESCRIBED_MEDICINES (id, medicine, prescription_date, instructions, dose, duration,
                                  frequency_of_administration, treatment_id)
VALUES (2, 'Enalapril', TIMESTAMP '2023-03-02 11:00:00', 'Tomar en ayunas', '10mg', 30, 'Una vez al día', 2);

-- Inicialización de TYPE_OF_TREATMENTS
INSERT INTO TYPE_OF_TREATMENTS (id, name, description, treatment_id)
VALUES (1, 'Farmacológico', 'Tratamiento basado en medicamentos', 1);
INSERT INTO TYPE_OF_TREATMENTS (id, name, description, treatment_id)
VALUES (2, 'Control y seguimiento', 'Monitoreo periódico de signos vitales', 2);

-- Inicialización de SPECIALITIES
INSERT INTO SPECIALITIES (id, name, description, doctor_id)
VALUES (1, 'Medicina General', 'Atención médica primaria', 2);
INSERT INTO SPECIALITIES (id, name, description, doctor_id)
VALUES (2, 'Cardiología', 'Especialista en el sistema cardiovascular', 5);

-- Inicialización de MEDICAL_TASKS
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (1, 'Toma de signos vitales', 10, 'Completada', 'Enfermero');
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (2, 'Registro de historia clínica', 15, 'Pendiente', 'Doctor');
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (3, 'Administración de medicamento', 5, 'Programada', 'Enfermero');

-- Inicialización de APPOINTMENT_TASKS
INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (1, 1);
INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (1, 2);
INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (2, 1);
INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (2, 3);

-- Sincronizar secuencias con el valor máximo actual

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));
SELECT setval('permissions_id_seq', (SELECT MAX(id) FROM permissions));
SELECT setval('personal_id_seq', (SELECT MAX(id) FROM personal));
SELECT setval('patients_id_seq', (SELECT MAX(id) FROM patients));
SELECT setval('medical_histories_id_seq', (SELECT MAX(id) FROM medical_histories));
SELECT setval('availabilities_id_seq', (SELECT MAX(id) FROM availabilities));
SELECT setval('statuses_id_seq', (SELECT MAX(id) FROM statuses));
SELECT setval('type_of_medical_appointments_id_seq', (SELECT MAX(id) FROM type_of_medical_appointments));
SELECT setval('appointment_results_id_seq', (SELECT MAX(id) FROM appointment_results));
SELECT setval('observations_id_seq', (SELECT MAX(id) FROM observations));
SELECT setval('treatments_id_seq', (SELECT MAX(id) FROM treatments));
SELECT setval('medical_appointments_id_seq', (SELECT MAX(id) FROM medical_appointments));
SELECT setval('medical_offices_id_seq', (SELECT MAX(id) FROM medical_offices));
SELECT setval('laboratories_id_seq', (SELECT MAX(id) FROM laboratories));
SELECT setval('type_of_exams_id_seq', (SELECT MAX(id) FROM type_of_exams));
SELECT setval('examination_results_id_seq', (SELECT MAX(id) FROM examination_results));
SELECT setval('prescribed_medicines_id_seq', (SELECT MAX(id) FROM prescribed_medicines));
SELECT setval('type_of_treatments_id_seq', (SELECT MAX(id) FROM type_of_treatments));
SELECT setval('specialities_id_seq', (SELECT MAX(id) FROM specialities));
SELECT setval('medical_tasks_id_seq', (SELECT MAX(id) FROM medical_tasks));
