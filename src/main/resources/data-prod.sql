-- USERS Initialization
INSERT INTO USERS (id, username, password)
VALUES (1, 'admin', '$2a$12$2TY0tg4iQBgTLDv8rhyReOS/lWg9pP8Aig88s0Q3jLaSO4JwUcLNy'); -- password: admin
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

INSERT INTO USERS (id, username, password)
VALUES (10, 'patient3', '$2a$12$kJbCtGPdd1SBYQVcuafQseS3HEuQPWyh1MjXBqRKjd0csmUfydeXi');
-- password: patient123
-- password: administrative123


-- Initializing ROLES
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


-- Initializing USER_ROLES
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
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (10, 3);
-- administrative1 -> ADMINISTRATIVE


-- Initializing PERMISSIONS
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

-- Initializing ROLE_PERMISSIONS
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

-- Initializing PERSONALS
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


-- Initialization of DOCTORS
INSERT INTO DOCTORS (id, medical_license_number)
VALUES (2, 'MED12345');
INSERT INTO DOCTORS (id, medical_license_number)
VALUES (5, 'MED54321');

-- Initializing ADMINISTRATIVE
INSERT INTO ADMINISTRATIVE (id, position, department)
VALUES (1, 'Coordinador', 'Administración');
INSERT INTO ADMINISTRATIVE (id, position, department)
VALUES (4, 'Asistente', 'Recursos Humanos');

-- Initializing PATIENTS
INSERT INTO PATIENTS (id, id_number, user_id, name, birthdate, gender, address, phone_number, email)
VALUES (1, 1111111111, 4, 'Ana López', TIMESTAMP '1985-05-10 00:00:00', 'F', 'Calle Principal 123', '555-6789',
        'ana.lopez@email.com');

INSERT INTO PATIENTS (id, id_number, user_id, name, birthdate, gender, address, phone_number, email)
VALUES (2, 1111111112, 5, 'Roberto Fernández', TIMESTAMP '1978-08-15 00:00:00', 'M', 'Avenida Central 456', '555-7890',
        'roberto.fernandez@email.com');

INSERT INTO PATIENTS (id, id_number, user_id, name, birthdate, gender, address, phone_number, email)
VALUES (3, 1111111113, 10, 'PACIENTE CERO', TIMESTAMP '1990-01-01 00:00:00', 'M', 'NUEVO PACIENTE CERO', '555-1111',
        'paciente.cero.no.tocar@email.com');

-- Initializing MEDICAL_HISTORIES
INSERT INTO MEDICAL_HISTORIES (id, patient_id, creation_date, current_health_status)
VALUES (1, 1, TIMESTAMP '2023-01-01 00:00:00', 'Saludable');
INSERT INTO MEDICAL_HISTORIES (id, patient_id, creation_date, current_health_status)
VALUES (2, 2, TIMESTAMP '2023-01-02 00:00:00', 'Tratamiento en curso');

-- Initializing STATUSES
INSERT INTO STATUSES (id, name)
VALUES (1, 'Disponible');
INSERT INTO STATUSES (id, name)
VALUES (2, 'Ocupado');
INSERT INTO STATUSES (id, name)
VALUES (3, 'Reservado');

-- Initializing AVAILABILITIES
INSERT INTO AVAILABILITIES (id, start_time, end_time, status_id)
VALUES (1, TIMESTAMP '2023-03-01 08:00:00', TIMESTAMP '2023-03-01 16:00:00', 1);
INSERT INTO AVAILABILITIES (id, start_time, end_time, status_id)
VALUES (2, TIMESTAMP '2023-03-02 08:00:00', TIMESTAMP '2023-03-02 16:00:00', 2);
INSERT INTO AVAILABILITIES (id, start_time, end_time, status_id)
VALUES (3, TIMESTAMP '2023-03-03 08:00:00', TIMESTAMP '2023-03-03 16:00:00', 3);

-- Initializing PERSONAL_AVAILABILITIES
INSERT INTO PERSONAL_AVAILABILITIES (personal_id, availability_id)
VALUES (2, 1);
INSERT INTO PERSONAL_AVAILABILITIES (personal_id, availability_id)
VALUES (2, 2);
INSERT INTO PERSONAL_AVAILABILITIES (personal_id, availability_id)
VALUES (5, 3);

-- Initializing TYPE_OF_MEDICAL_APPOINTMENTS
INSERT INTO TYPE_OF_MEDICAL_APPOINTMENTS (id, name)
VALUES (1, 'Consulta general'),
       (2, 'Revisión de rutina'),
       (3, 'Consulta especializada'),
       (4, 'Consulta oncológica inicial'),
       (5, 'Sesión de quimioterapia'),
       (6, 'Sesión de radioterapia'),
       (7, 'Control post-tratamiento'),
       (8, 'Consulta con nutricionista oncológico'),
       (9, 'Apoyo psicológico');

-- Initializing APPOINTMENT_RESULTS
INSERT INTO APPOINTMENT_RESULTS (id, evaluation_date, medical_history_id)
VALUES (1, TIMESTAMP '2023-03-01 09:30:00', 1);
INSERT INTO APPOINTMENT_RESULTS (id, evaluation_date, medical_history_id)
VALUES (2, TIMESTAMP '2023-03-02 10:45:00', 2);

-- Initializing OBSERVATIONS
INSERT INTO OBSERVATIONS (id, content, recommendation, appointment_result_id)
VALUES (1, 'Paciente presenta síntomas de resfriado común', 'Reposo y abundante líquido', 1);
INSERT INTO OBSERVATIONS (id, content, recommendation, appointment_result_id)
VALUES (2, 'Control de presión arterial', 'Continuar con medicación actual', 2);

-- Initializing TREATMENTS
INSERT INTO TREATMENTS (id, name, description, date_start, end_date, appointment_result_id)
VALUES (1, 'Tratamiento para resfriado', 'Medicamentos para aliviar síntomas', TIMESTAMP '2023-03-01 10:00:00',
        TIMESTAMP '2023-03-08 23:59:59', 1);
INSERT INTO TREATMENTS (id, name, description, date_start, end_date, appointment_result_id)
VALUES (2, 'Control de hipertensión', 'Seguimiento de presión arterial', TIMESTAMP '2023-03-02 11:00:00',
        TIMESTAMP '2023-04-02 23:59:59', 2);

INSERT INTO MEDICAL_OFFICES (id, name, location, equipment)
VALUES (1, 'Consultorio 1', 'Primer piso - Ala Norte', 'Básico: camilla, escritorio, sillas'),
       (2, 'Consultorio 2', 'Primer piso - Ala Sur', 'Básico: camilla, escritorio, sillas, tensiómetro'),
       (3, 'Sala de Quimioterapia A', 'Segundo piso - Oncología',
        'Bomba de infusión, monitor cardíaco, cama reclinable'),
       (4, 'Sala de Quimioterapia B', 'Segundo piso - Oncología',
        'Bomba de infusión, monitor cardíaco, cama reclinable'),
       (5, 'Consultorio Nutrición', 'Primer piso - Ala Este', 'Báscula, medidor de composición corporal, escritorio'),
       (6, 'Consultorio Psicología', 'Tercer piso - Bienestar', 'Sillones cómodos, ambiente relajante, escritorio'),
       (7, 'Sala de Procedimientos', 'Primer piso - Centro', 'Camilla de procedimientos, equipos de cirugía menor');


-- Initializing LABORATORIES
INSERT INTO LABORATORIES (id, name, location, telephone)
VALUES (1, 'Lab Central', 'Edificio A, Planta 2', '555-8901');
INSERT INTO LABORATORIES (id, name, location, telephone)
VALUES (2, 'Lab Especializado', 'Edificio B, Planta 1', '555-9012');

-- Initializing TYPE_OF_EXAMS
INSERT INTO TYPE_OF_EXAMS (id, name, description)
VALUES (1, 'Análisis de sangre', 'Hemograma completo');
INSERT INTO TYPE_OF_EXAMS (id, name, description)
VALUES (2, 'Radiografía', 'Imagen de tórax');

-- Initializing MEDICAL_EXAMINATIONS
INSERT INTO MEDICAL_EXAMINATIONS (id, date_of_realization, laboratory_id, type_of_exam_id, medical_history_id)
VALUES ('EX0001', TIMESTAMP '2023-03-03 11:00:00', 1, 1, 1);
INSERT INTO MEDICAL_EXAMINATIONS (id, date_of_realization, laboratory_id, type_of_exam_id, medical_history_id)
VALUES ('EX0002', TIMESTAMP '2023-03-04 14:30:00', 2, 2, 2);

-- Initializing EXAMINATION_RESULTS
-- INSERT INTO EXAMINATION_RESULTS (id, generation_date, results_report, medical_history_id)
-- VALUES (1, TIMESTAMP '2023-03-03 15:00:00', pg_read_binary_file('/docker-entrypoint-init-db.d/files/example-1.jpg'), 1);
-- INSERT INTO EXAMINATION_RESULTS (id, generation_date, results_report, medical_history_id)
-- VALUES (2, TIMESTAMP '2023-03-04 17:00:00', pg_read_binary_file('/docker-entrypoint-init-db.d/files/example-2.png'), 2);

-- Initializing PRESCRIBED_MEDICINES
INSERT INTO PRESCRIBED_MEDICINES (id, medicine, prescription_date, instructions, dose, duration,
                                  frequency_of_administration, treatment_id)
VALUES (1, 'Paracetamol', TIMESTAMP '2023-03-01 10:00:00', 'Tomar con las comidas', '500mg', 7, 'Cada 8 horas', 1);
INSERT INTO PRESCRIBED_MEDICINES (id, medicine, prescription_date, instructions, dose, duration,
                                  frequency_of_administration, treatment_id)
VALUES (2, 'Enalapril', TIMESTAMP '2023-03-02 11:00:00', 'Tomar en ayunas', '10mg', 30, 'Una vez al día', 2);

-- Initializing TYPE_OF_TREATMENTS
INSERT INTO TYPE_OF_TREATMENTS (id, name, description, treatment_id)
VALUES (1, 'Farmacológico', 'Tratamiento basado en medicamentos', 1);
INSERT INTO TYPE_OF_TREATMENTS (id, name, description, treatment_id)
VALUES (2, 'Control y seguimiento', 'Monitoreo periódico de signos vitales', 2);

-- Initializing SPECIALITIES
INSERT INTO SPECIALITIES (id, name, description)
VALUES (1, 'Medicina General', 'Atención médica primaria');
INSERT INTO SPECIALITIES (id, name, description)
VALUES (2, 'Cardiología', 'Especialista en el sistema cardiovascular');

INSERT INTO DOCTOR_SPECIALITY (doctor_id, speciality_id)
VALUES (2, 1);
INSERT INTO DOCTOR_SPECIALITY (doctor_id, speciality_id)
VALUES (5, 2);

-- =====================================================
-- DATOS DE CITAS MÉDICAS PROTOCOLARIAS
-- =====================================================

-- 1. Crear consultorio específico para citas protocolarias
INSERT INTO MEDICAL_OFFICES (id, name, location, equipment)
VALUES (1000000000000, 'Consultorio Protocolos Médicos', 'Área de Desarrollo - Solo para Protocolos',
        'Consultorio virtual para definición de protocolos y tareas médicas estándar');

-- 2. Crear historia médica para el PACIENTE CERO (ID: 3)
INSERT INTO MEDICAL_HISTORIES (id, patient_id, creation_date, current_health_status)
VALUES (1000000000000, 3, TIMESTAMP '1990-01-01 00:00:00',
        'Paciente en evaluación inicial - Sin diagnóstico establecido');

-- 3. Crear citas médicas para cada tipo (1 de enero de 1990, con diferencia de 1 hora)
INSERT INTO MEDICAL_APPOINTMENTS (id, doctor_id, type_of_medical_appointment_id, appointment_date, treatment_id,
                                  medical_history_id, medical_office_id)
VALUES (1, 5, 1, TIMESTAMP '1990-01-01 08:00:00', NULL, 1000000000000, 1000000000000), -- Consulta general
       (2, 5, 2, TIMESTAMP '1990-01-01 09:00:00', NULL, 1000000000000, 1000000000000), -- Revisión de rutina
       (3, 5, 3, TIMESTAMP '1990-01-01 10:00:00', NULL, 1000000000000, 1000000000000), -- Consulta especializada
       (4, 5, 4, TIMESTAMP '1990-01-01 11:00:00', NULL, 1000000000000, 1000000000000), -- Consulta oncológica inicial
       (5, 5, 5, TIMESTAMP '1990-01-01 12:00:00', NULL, 1000000000000, 1000000000000), -- Sesión de quimioterapia
       (6, 5, 6, TIMESTAMP '1990-01-01 13:00:00', NULL, 1000000000000, 1000000000000), -- Sesión de radioterapia
       (7, 5, 7, TIMESTAMP '1990-01-01 14:00:00', NULL, 1000000000000, 1000000000000), -- Control post-tratamiento
       (8, 5, 8, TIMESTAMP '1990-01-01 15:00:00', NULL, 1000000000000,
        1000000000000),                                                                -- Consulta con nutricionista oncológico
       (9, 5, 9, TIMESTAMP '1990-01-01 16:00:00', NULL, 1000000000000, 1000000000000);
-- Apoyo psicológico

-- 3. TAREAS MÉDICAS PROTOCOLARIAS

-- =====================================================
-- CONSULTA GENERAL (Cita ID: 1)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (1, 'Revisar historia clínica previa', 10, 'PENDIENTE', 'Doctor'),
       (2, 'Realizar anamnesis completa', 20, 'PENDIENTE', 'Doctor'),
       (3, 'Examen físico general', 15, 'PENDIENTE', 'Doctor'),
       (4, 'Toma de signos vitales', 5, 'PENDIENTE', 'Enfermería'),
       (5, 'Evaluar síntomas actuales', 10, 'PENDIENTE', 'Doctor'),
       (6, 'Documentar hallazgos en historia clínica', 5, 'PENDIENTE', 'Doctor'),
       (7, 'Definir plan de manejo inicial', 10, 'PENDIENTE', 'Doctor');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7);

-- =====================================================
-- REVISIÓN DE RUTINA (Cita ID: 2)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (8, 'Verificar adherencia al tratamiento', 5, 'PENDIENTE', 'Doctor'),
       (9, 'Revisar estudios de laboratorio recientes', 10, 'PENDIENTE', 'Doctor'),
       (10, 'Examen físico dirigido', 10, 'PENDIENTE', 'Doctor'),
       (11, 'Evaluación de efectos adversos', 8, 'PENDIENTE', 'Doctor'),
       (12, 'Ajuste de medicación si es necesario', 5, 'PENDIENTE', 'Doctor'),
       (13, 'Programar próxima cita de seguimiento', 3, 'PENDIENTE', 'Administrativo'),
       (14, 'Actualizar registro médico', 5, 'PENDIENTE', 'Doctor');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (2, 8),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12),
       (2, 13),
       (2, 14);

-- =====================================================
-- CONSULTA ESPECIALIZADA (Cita ID: 3)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (15, 'Revisar motivo de interconsulta', 5, 'PENDIENTE', 'Doctor'),
       (16, 'Análisis detallado de estudios complementarios', 15, 'PENDIENTE', 'Doctor'),
       (17, 'Examen físico especializado por sistemas', 20, 'PENDIENTE', 'Doctor'),
       (18, 'Correlación clínico-patológica', 10, 'PENDIENTE', 'Doctor'),
       (19, 'Solicitar estudios adicionales si procede', 5, 'PENDIENTE', 'Doctor'),
       (20, 'Formular impresión diagnóstica', 10, 'PENDIENTE', 'Doctor'),
       (21, 'Elaborar plan terapéutico especializado', 15, 'PENDIENTE', 'Doctor'),
       (22, 'Comunicar hallazgos al médico tratante', 5, 'PENDIENTE', 'Doctor');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (3, 15),
       (3, 16),
       (3, 17),
       (3, 18),
       (3, 19),
       (3, 20),
       (3, 21),
       (3, 22);

-- =====================================================
-- CONSULTA ONCOLÓGICA INICIAL (Cita ID: 4)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (23, 'Revisar biopsia e informes patológicos', 15, 'PENDIENTE', 'Doctor'),
       (24, 'Estadificación completa del tumor', 20, 'PENDIENTE', 'Doctor'),
       (25, 'Evaluación del estado funcional (ECOG)', 5, 'PENDIENTE', 'Doctor'),
       (26, 'Análisis de comorbilidades', 10, 'PENDIENTE', 'Doctor'),
       (27, 'Discusión de opciones terapéuticas', 20, 'PENDIENTE', 'Doctor'),
       (28, 'Obtención de consentimiento informado', 10, 'PENDIENTE', 'Doctor'),
       (29, 'Programación en junta médica multidisciplinaria', 5, 'PENDIENTE', 'Administrativo'),
       (30, 'Solicitud de estudios de extensión', 10, 'PENDIENTE', 'Doctor'),
       (31, 'Coordinar con otros especialistas', 5, 'PENDIENTE', 'Administrativo');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (4, 23),
       (4, 24),
       (4, 25),
       (4, 26),
       (4, 27),
       (4, 28),
       (4, 29),
       (4, 30),
       (4, 31);

-- =====================================================
-- SESIÓN DE QUIMIOTERAPIA (Cita ID: 5)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (32, 'Verificar laboratorios pre-quimioterapia', 10, 'PENDIENTE', 'Enfermería Oncológica'),
       (33, 'Evaluar toxicidades del ciclo anterior', 10, 'PENDIENTE', 'Doctor'),
       (34, 'Verificar estado funcional actual', 5, 'PENDIENTE', 'Doctor'),
       (35, 'Autorizar inicio de quimioterapia', 5, 'PENDIENTE', 'Doctor'),
       (36, 'Preparar medicamentos según protocolo', 30, 'PENDIENTE', 'Farmacia Oncológica'),
       (37, 'Administrar premedicación', 15, 'PENDIENTE', 'Enfermería Oncológica'),
       (38, 'Infundir quimioterapia según esquema', 180, 'PENDIENTE', 'Enfermería Oncológica'),
       (39, 'Monitoreo continuo durante infusión', 180, 'PENDIENTE', 'Enfermería Oncológica'),
       (40, 'Registro de efectos adversos', 5, 'PENDIENTE', 'Enfermería Oncológica'),
       (41, 'Medicación post-quimioterapia', 10, 'PENDIENTE', 'Enfermería Oncológica'),
       (42, 'Programar próximo ciclo', 5, 'PENDIENTE', 'Administrativo');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (5, 32),
       (5, 33),
       (5, 34),
       (5, 35),
       (5, 36),
       (5, 37),
       (5, 38),
       (5, 39),
       (5, 40),
       (5, 41),
       (5, 42);

-- =====================================================
-- SESIÓN DE RADIOTERAPIA (Cita ID: 6)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (43, 'Verificar plan de tratamiento radioterápico', 10, 'PENDIENTE', 'Radioterapeuta'),
       (44, 'Evaluar toxicidades cutáneas', 5, 'PENDIENTE', 'Radioterapeuta'),
       (45, 'Posicionamiento del paciente', 10, 'PENDIENTE', 'Técnico en Radioterapia'),
       (46, 'Verificación de marcadores anatómicos', 5, 'PENDIENTE', 'Técnico en Radioterapia'),
       (47, 'Calibración del equipo', 5, 'PENDIENTE', 'Físico Médico'),
       (48, 'Aplicación de radioterapia', 15, 'PENDIENTE', 'Técnico en Radioterapia'),
       (49, 'Documentar dosis administrada', 3, 'PENDIENTE', 'Técnico en Radioterapia'),
       (50, 'Evaluación post-radiación inmediata', 5, 'PENDIENTE', 'Radioterapeuta'),
       (51, 'Indicaciones para cuidados en casa', 5, 'PENDIENTE', 'Radioterapeuta');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (6, 43),
       (6, 44),
       (6, 45),
       (6, 46),
       (6, 47),
       (6, 48),
       (6, 49),
       (6, 50),
       (6, 51);

-- =====================================================
-- CONTROL POST-TRATAMIENTO (Cita ID: 7)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (52, 'Revisar estudios de control imagenológicos', 15, 'PENDIENTE', 'Doctor'),
       (53, 'Evaluar respuesta al tratamiento', 10, 'PENDIENTE', 'Doctor'),
       (54, 'Examen físico completo post-tratamiento', 15, 'PENDIENTE', 'Doctor'),
       (55, 'Detección de toxicidades tardías', 10, 'PENDIENTE', 'Doctor'),
       (56, 'Evaluación nutricional post-tratamiento', 8, 'PENDIENTE', 'Doctor'),
       (57, 'Valoración psicológica y calidad de vida', 10, 'PENDIENTE', 'Doctor'),
       (58, 'Solicitar marcadores tumorales', 3, 'PENDIENTE', 'Doctor'),
       (59, 'Planificar seguimiento a largo plazo', 10, 'PENDIENTE', 'Doctor'),
       (60, 'Educación para signos de alarma', 10, 'PENDIENTE', 'Doctor'),
       (61, 'Coordinar rehabilitación si es necesaria', 5, 'PENDIENTE', 'Administrativo');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (7, 52),
       (7, 53),
       (7, 54),
       (7, 55),
       (7, 56),
       (7, 57),
       (7, 58),
       (7, 59),
       (7, 60),
       (7, 61);

-- =====================================================
-- CONSULTA CON NUTRICIONISTA ONCOLÓGICO (Cita ID: 8)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (62, 'Evaluar estado nutricional actual', 15, 'PENDIENTE', 'Nutricionista'),
       (63, 'Análisis de ingesta alimentaria', 10, 'PENDIENTE', 'Nutricionista'),
       (64, 'Revisar pérdida de peso reciente', 5, 'PENDIENTE', 'Nutricionista'),
       (65, 'Evaluar síntomas gastrointestinales', 8, 'PENDIENTE', 'Nutricionista'),
       (66, 'Medir composición corporal', 10, 'PENDIENTE', 'Nutricionista'),
       (67, 'Calcular requerimientos nutricionales', 10, 'PENDIENTE', 'Nutricionista'),
       (68, 'Diseñar plan alimentario personalizado', 20, 'PENDIENTE', 'Nutricionista'),
       (69, 'Educación nutricional oncológica', 15, 'PENDIENTE', 'Nutricionista'),
       (70, 'Prescribir suplementos si es necesario', 5, 'PENDIENTE', 'Nutricionista'),
       (71, 'Programar seguimiento nutricional', 3, 'PENDIENTE', 'Nutricionista');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (8, 62),
       (8, 63),
       (8, 64),
       (8, 65),
       (8, 66),
       (8, 67),
       (8, 68),
       (8, 69),
       (8, 70),
       (8, 71);

-- =====================================================
-- APOYO PSICOLÓGICO (Cita ID: 9)
-- =====================================================
INSERT INTO MEDICAL_TASKS (id, description, estimated_time, status, responsible)
VALUES (72, 'Evaluación del estado emocional', 15, 'PENDIENTE', 'Psicólogo'),
       (73, 'Explorar mecanismos de afrontamiento', 20, 'PENDIENTE', 'Psicólogo'),
       (74, 'Detectar signos de depresión/ansiedad', 15, 'PENDIENTE', 'Psicólogo'),
       (75, 'Evaluar soporte familiar y social', 10, 'PENDIENTE', 'Psicólogo'),
       (76, 'Aplicar escalas psicométricas', 10, 'PENDIENTE', 'Psicólogo'),
       (77, 'Psicoeducación sobre proceso oncológico', 20, 'PENDIENTE', 'Psicólogo'),
       (78, 'Enseñar técnicas de relajación', 15, 'PENDIENTE', 'Psicólogo'),
       (79, 'Fortalecer red de apoyo', 10, 'PENDIENTE', 'Psicólogo'),
       (80, 'Planificar intervención psicológica', 10, 'PENDIENTE', 'Psicólogo'),
       (81, 'Agendar sesiones de seguimiento', 5, 'PENDIENTE', 'Psicólogo');

INSERT INTO APPOINTMENT_TASKS (medical_appointment_id, medical_task_id)
VALUES (9, 72),
       (9, 73),
       (9, 74),
       (9, 75),
       (9, 76),
       (9, 77),
       (9, 78),
       (9, 79),
       (9, 80),
       (9, 81);

-- Synchronize sequences with the current maximum value

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));
SELECT setval('permissions_id_seq', (SELECT MAX(id) FROM permissions));
SELECT setval('personal_id_seq', (SELECT MAX(id) FROM personal));
SELECT setval('patients_id_seq', (SELECT MAX(id) FROM patients));
SELECT setval('medical_histories_id_seq', 20);
SELECT setval('availabilities_id_seq', (SELECT MAX(id) FROM availabilities));
SELECT setval('statuses_id_seq', (SELECT MAX(id) FROM statuses));
SELECT setval('type_of_medical_appointments_id_seq', (SELECT MAX(id) FROM type_of_medical_appointments));
SELECT setval('appointment_results_id_seq', (SELECT MAX(id) FROM appointment_results));
SELECT setval('observations_id_seq', (SELECT MAX(id) FROM observations));
SELECT setval('treatments_id_seq', (SELECT MAX(id) FROM treatments));
SELECT setval('medical_appointments_id_seq', (SELECT MAX(id) FROM medical_appointments));
SELECT setval('medical_offices_id_seq', 20);
SELECT setval('laboratories_id_seq', (SELECT MAX(id) FROM laboratories));
SELECT setval('type_of_exams_id_seq', (SELECT MAX(id) FROM type_of_exams));
SELECT setval('examination_results_id_seq', (SELECT MAX(id) FROM examination_results));
SELECT setval('prescribed_medicines_id_seq', (SELECT MAX(id) FROM prescribed_medicines));
SELECT setval('type_of_treatments_id_seq', (SELECT MAX(id) FROM type_of_treatments));
SELECT setval('specialities_id_seq', (SELECT MAX(id) FROM specialities));
SELECT setval('medical_tasks_id_seq', (SELECT MAX(id) FROM medical_tasks));
