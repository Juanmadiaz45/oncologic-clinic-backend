package com.oncologic.clinic.service.examination;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class MedicalReportGenerator {

    private final Random random = new Random();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String generateReport(String examType, String laboratoryName, String patientName) {
        String reportType = determineReportType(examType);

        switch (reportType.toLowerCase()) {
            case "sangre":
                return generateBloodTestReport(laboratoryName, patientName);
            case "radiografia":
                return generateRadiologyReport(laboratoryName, patientName);
            case "orina":
                return generateUrineTestReport(laboratoryName, patientName);
            case "oncologia":
                return generateOncologyReport(laboratoryName, patientName);
            case "cardiologia":
                return generateCardiologyReport(laboratoryName, patientName);
            default:
                return generateGeneralReport(laboratoryName, patientName);
        }
    }

    private String determineReportType(String examType) {
        if (examType == null) return "general";

        String type = examType.toLowerCase();
        if (type.contains("sangre") || type.contains("hemograma") || type.contains("hematología")) {
            return "sangre";
        } else if (type.contains("radiografía") || type.contains("rayos") || type.contains("rx")) {
            return "radiografia";
        } else if (type.contains("orina") || type.contains("urinálisis")) {
            return "orina";
        } else if (type.contains("oncol") || type.contains("tumor") || type.contains("biopsia")) {
            return "oncologia";
        } else if (type.contains("cardio") || type.contains("ecg") || type.contains("corazón")) {
            return "cardiologia";
        }
        return "general";
    }

    private String generateBloodTestReport(String laboratoryName, String patientName) {
        return String.format("""
            =====================================
            %s
            =====================================
            
            REPORTE DE ANÁLISIS DE SANGRE
            
            Paciente: %s
            Fecha: %s
            
            HEMOGRAMA COMPLETO
            ==================
            
            Glóbulos Rojos:      %.2f millones/μL    (Ref: 4.5-5.5)
            Hemoglobina:         %.1f g/dL           (Ref: 12.0-16.0)
            Hematocrito:         %.1f%%              (Ref: 36-46)
            Glóbulos Blancos:    %.1f x10³/μL        (Ref: 4.5-11.0)
            Plaquetas:           %d x10³/μL          (Ref: 150-450)
            
            QUÍMICA SANGUÍNEA
            =================
            
            Glucosa:             %d mg/dL            (Ref: 70-110)
            Colesterol Total:    %d mg/dL            (Ref: <200)
            Triglicéridos:       %d mg/dL            (Ref: <150)
            Creatinina:          %.2f mg/dL          (Ref: 0.6-1.2)
            Urea:                %d mg/dL            (Ref: 15-45)
            
            OBSERVACIONES:
            %s
            
            CONCLUSIÓN:
            %s
            
            =====================================
            Dr. %s
            Patólogo Clínico
            Lic. Med. %d
            %s
            =====================================
            """,
                laboratoryName,
                patientName,
                LocalDateTime.now().format(formatter),
                generateRandomValue(4.2, 5.8, 2),
                generateRandomValue(11.5, 16.5, 1),
                generateRandomValue(35.0, 47.0, 1),
                generateRandomValue(4.0, 12.0, 1),
                (int) generateRandomValue(140, 480, 0),
                (int) generateRandomValue(65, 125, 0),
                (int) generateRandomValue(150, 220, 0),
                (int) generateRandomValue(80, 180, 0),
                generateRandomValue(0.5, 1.3, 2),
                (int) generateRandomValue(12, 48, 0),
                getRandomBloodObservation(),
                getRandomBloodConclusion(),
                getRandomDoctorName(),
                random.nextInt(9000) + 1000,
                laboratoryName
        );
    }

    private String generateRadiologyReport(String laboratoryName, String patientName) {
        return String.format("""
            =====================================
            %s
            DEPARTAMENTO DE RADIOLOGÍA
            =====================================
            
            REPORTE RADIOLÓGICO
            
            Paciente: %s
            Fecha: %s
            Estudio: Radiografía de Tórax PA y Lateral
            
            TÉCNICA:
            Se realizan radiografías de tórax en proyecciones 
            posteroanterior y lateral con técnica estándar.
            
            HALLAZGOS:
            
            Silueta cardíaca: %s
            
            Campos pulmonares: %s
            
            Hilios pulmonares: %s
            
            Estructuras óseas: %s
            
            Espacios pleurales: %s
            
            IMPRESIÓN DIAGNÓSTICA:
            %s
            
            RECOMENDACIONES:
            %s
            
            =====================================
            Dr. %s
            Radiólogo
            Lic. Med. %d
            %s
            =====================================
            """,
                laboratoryName,
                patientName,
                LocalDateTime.now().format(formatter),
                getRandomRadiologyFinding("cardiac"),
                getRandomRadiologyFinding("lungs"),
                getRandomRadiologyFinding("hilum"),
                getRandomRadiologyFinding("bones"),
                getRandomRadiologyFinding("pleura"),
                getRandomRadiologyConclusion(),
                getRandomRadiologyRecommendation(),
                getRandomDoctorName(),
                random.nextInt(9000) + 1000,
                laboratoryName
        );
    }

    private String generateUrineTestReport(String laboratoryName, String patientName) {
        return String.format("""
            =====================================
            %s
            =====================================
            
            REPORTE DE ANÁLISIS DE ORINA
            
            Paciente: %s
            Fecha: %s
            
            EXAMEN FÍSICO
            =============
            
            Color:               %s
            Aspecto:             %s
            Densidad:            %.3f               (Ref: 1.010-1.025)
            pH:                  %.1f                (Ref: 5.0-8.0)
            
            EXAMEN QUÍMICO
            ==============
            
            Proteínas:           %s                 (Ref: Negativo)
            Glucosa:             %s                 (Ref: Negativo)
            Cetonas:             %s                 (Ref: Negativo)
            Sangre:              %s                 (Ref: Negativo)
            Nitritos:            %s                 (Ref: Negativo)
            Leucocitos:          %s                 (Ref: Negativo)
            
            EXAMEN MICROSCÓPICO
            ===================
            
            Leucocitos:          %d por campo       (Ref: 0-5)
            Eritrocitos:         %d por campo       (Ref: 0-2)
            Células epiteliales: %s
            Bacterias:           %s
            Cristales:           %s
            
            OBSERVACIONES:
            %s
            
            CONCLUSIÓN:
            %s
            
            =====================================
            Bioanalista %s
            Lic. Bioanalisis %d
            %s
            =====================================
            """,
                laboratoryName,
                patientName,
                LocalDateTime.now().format(formatter),
                getRandomUrineColor(),
                getRandomUrineAspect(),
                generateRandomValue(1.008, 1.028, 3),
                generateRandomValue(5.5, 7.5, 1),
                getRandomUrineResult(),
                getRandomUrineResult(),
                getRandomUrineResult(),
                getRandomUrineResult(),
                getRandomUrineResult(),
                getRandomUrineResult(),
                (int) generateRandomValue(0, 8, 0),
                (int) generateRandomValue(0, 4, 0),
                getRandomMicroscopicFinding(),
                getRandomMicroscopicFinding(),
                getRandomMicroscopicFinding(),
                getRandomUrineObservation(),
                getRandomUrineConclusion(),
                getRandomTechnicianName(),
                random.nextInt(9000) + 1000,
                laboratoryName
        );
    }

    private String generateOncologyReport(String laboratoryName, String patientName) {
        return String.format("""
            =====================================
            %s
            DEPARTAMENTO DE PATOLOGÍA ONCOLÓGICA
            =====================================
            
            REPORTE HISTOPATOLÓGICO
            
            Paciente: %s
            Fecha: %s
            Muestra: %s
            
            DESCRIPCIÓN MACROSCÓPICA:
            %s
            
            DESCRIPCIÓN MICROSCÓPICA:
            %s
            
            TÉCNICAS ESPECIALES:
            %s
            
            MARCADORES TUMORALES:
            %s
            
            DIAGNÓSTICO:
            %s
            
            CLASIFICACIÓN TNM:
            %s
            
            GRADO HISTOLÓGICO:
            %s
            
            COMENTARIOS:
            %s
            
            RECOMENDACIONES:
            %s
            
            =====================================
            Dr. %s
            Patólogo Oncólogo
            Lic. Med. %d
            %s
            =====================================
            """,
                laboratoryName,
                patientName,
                LocalDateTime.now().format(formatter),
                getRandomOncologySample(),
                getRandomMacroscopicDescription(),
                getRandomMicroscopicDescription(),
                getRandomSpecialTechniques(),
                getRandomTumorMarkers(),
                getRandomOncologyDiagnosis(),
                getRandomTNMClassification(),
                getRandomHistologicGrade(),
                getRandomOncologyComments(),
                getRandomOncologyRecommendations(),
                getRandomDoctorName(),
                random.nextInt(9000) + 1000,
                laboratoryName
        );
    }

    private String generateCardiologyReport(String laboratoryName, String patientName) {
        return String.format("""
            =====================================
            %s
            DEPARTAMENTO DE CARDIOLOGÍA
            =====================================
            
            ELECTROCARDIOGRAMA
            
            Paciente: %s
            Fecha: %s
            
            PARÁMETROS TÉCNICOS:
            Velocidad: 25 mm/s
            Voltaje: 10 mm/mV
            Filtro: 40 Hz
            
            MEDICIONES:
            ===========
            
            Frecuencia Cardíaca: %d lpm           (Ref: 60-100)
            Ritmo:               %s
            Eje QRS:             %s
            Intervalo PR:        %d ms             (Ref: 120-200)
            Duración QRS:        %d ms             (Ref: 80-120)
            Intervalo QT:        %d ms             (Ref: 350-440)
            QTc (Bazett):        %d ms             (Ref: <450)
            
            ANÁLISIS POR DERIVACIONES:
            =========================
            
            Derivaciones de miembros: %s
            
            Derivaciones precordiales: %s
            
            INTERPRETACIÓN:
            %s
            
            CONCLUSIÓN:
            %s
            
            RECOMENDACIONES:
            %s
            
            =====================================
            Dr. %s
            Cardiólogo
            Lic. Med. %d
            %s
            =====================================
            """,
                laboratoryName,
                patientName,
                LocalDateTime.now().format(formatter),
                (int) generateRandomValue(55, 105, 0),
                getRandomHeartRhythm(),
                getRandomQRSAxis(),
                (int) generateRandomValue(110, 210, 0),
                (int) generateRandomValue(75, 125, 0),
                (int) generateRandomValue(340, 450, 0),
                (int) generateRandomValue(380, 460, 0),
                getRandomECGFinding("limb"),
                getRandomECGFinding("precordial"),
                getRandomECGInterpretation(),
                getRandomECGConclusion(),
                getRandomECGRecommendations(),
                getRandomDoctorName(),
                random.nextInt(9000) + 1000,
                laboratoryName
        );
    }

    private String generateGeneralReport(String laboratoryName, String patientName) {
        return String.format("""
            =====================================
            %s
            =====================================
            
            REPORTE DE EXAMEN MÉDICO
            
            Paciente: %s
            Fecha: %s
            
            RESULTADOS GENERALES:
            ====================
            
            Parámetro A:         %.1f mg/dL         (Ref: 50-150)
            Parámetro B:         %.2f g/dL          (Ref: 5-15)
            Parámetro C:         %d U/L             (Ref: 100-300)
            Parámetro D:         %.1f mmol/L        (Ref: 2-8)
            
            OBSERVACIONES:
            %s
            
            CONCLUSIÓN:
            %s
            
            RECOMENDACIONES:
            %s
            
            =====================================
            Dr. %s
            Médico Especialista
            Lic. Med. %d
            %s
            =====================================
            """,
                laboratoryName,
                patientName,
                LocalDateTime.now().format(formatter),
                generateRandomValue(45, 155, 1),
                generateRandomValue(4.5, 16.0, 2),
                (int) generateRandomValue(95, 310, 0),
                generateRandomValue(1.8, 8.5, 1),
                getRandomGeneralObservation(),
                getRandomGeneralConclusion(),
                getRandomGeneralRecommendation(),
                getRandomDoctorName(),
                random.nextInt(9000) + 1000,
                laboratoryName
        );
    }

    // Métodos auxiliares para generar valores aleatorios
    private double generateRandomValue(double min, double max, int decimals) {
        double value = min + (max - min) * random.nextDouble();
        return Math.round(value * Math.pow(10, decimals)) / Math.pow(10, decimals);
    }

    // Métodos para obtener valores aleatorios específicos
    private String getRandomBloodObservation() {
        String[] observations = {
                "Valores dentro de los parámetros normales.",
                "Ligera variación en algunos parámetros que requiere seguimiento.",
                "Resultados satisfactorios para la edad del paciente.",
                "Se observan valores límite que requieren monitoreo.",
                "Perfil hematológico estable."
        };
        return observations[random.nextInt(observations.length)];
    }

    private String getRandomBloodConclusion() {
        String[] conclusions = {
                "Hemograma dentro de límites normales.",
                "Química sanguínea sin alteraciones significativas.",
                "Resultados compatibles con estado de salud normal.",
                "Valores estables, continuar seguimiento de rutina.",
                "Perfil analítico satisfactorio."
        };
        return conclusions[random.nextInt(conclusions.length)];
    }

    private String getRandomRadiologyFinding(String type) {
        switch (type) {
            case "cardiac":
                return random.nextBoolean() ?
                        "Tamaño y morfología dentro de límites normales" :
                        "Ligero aumento del índice cardiotorácico";
            case "lungs":
                return random.nextBoolean() ?
                        "Campos pulmonares libres, sin infiltrados" :
                        "Tenue aumento de la trama broncovascular";
            case "hilum":
                return random.nextBoolean() ?
                        "Hilios de tamaño y densidad normales" :
                        "Hilios ligeramente prominentes";
            case "bones":
                return random.nextBoolean() ?
                        "Estructuras óseas sin alteraciones" :
                        "Cambios degenerativos menores";
            case "pleura":
                return random.nextBoolean() ?
                        "Espacios pleurales libres" :
                        "Mínimo engrosamiento pleural basal";
            default:
                return "Sin alteraciones significativas";
        }
    }

    private String getRandomRadiologyConclusion() {
        String[] conclusions = {
                "Radiografía de tórax sin hallazgos patológicos significativos.",
                "Estudio dentro de límites normales para la edad.",
                "Sin evidencia de patología aguda.",
                "Hallazgos inespecíficos que requieren correlación clínica.",
                "Estudio satisfactorio."
        };
        return conclusions[random.nextInt(conclusions.length)];
    }

    private String getRandomRadiologyRecommendation() {
        String[] recommendations = {
                "Control radiológico de rutina según criterio clínico.",
                "Correlacionar con hallazgos clínicos.",
                "Seguimiento radiológico en 6-12 meses si persisten síntomas.",
                "No requiere seguimiento radiológico inmediato.",
                "Control evolutivo según evolución clínica."
        };
        return recommendations[random.nextInt(recommendations.length)];
    }

    private String getRandomUrineColor() {
        String[] colors = {"Amarillo claro", "Amarillo", "Amarillo pálido"};
        return colors[random.nextInt(colors.length)];
    }

    private String getRandomUrineAspect() {
        String[] aspects = {"Claro", "Ligeramente turbio", "Transparente"};
        return aspects[random.nextInt(aspects.length)];
    }

    private String getRandomUrineResult() {
        return random.nextBoolean() ? "Negativo" : "Trazas";
    }

    private String getRandomMicroscopicFinding() {
        String[] findings = {"Escasas", "Moderadas", "Ausentes", "Raras"};
        return findings[random.nextInt(findings.length)];
    }

    private String getRandomUrineObservation() {
        String[] observations = {
                "Examen de orina dentro de parámetros normales.",
                "Sedimento urinario sin alteraciones significativas.",
                "Resultados compatibles con función renal normal.",
                "Análisis satisfactorio para seguimiento rutinario."
        };
        return observations[random.nextInt(observations.length)];
    }

    private String getRandomUrineConclusion() {
        String[] conclusions = {
                "Análisis de orina normal.",
                "Sin evidencia de infección urinaria.",
                "Función renal aparentemente normal.",
                "Sedimento urinario sin alteraciones."
        };
        return conclusions[random.nextInt(conclusions.length)];
    }

    private String getRandomDoctorName() {
        String[] names = {
                "Carlos Mendoza", "Ana Rodríguez", "Luis García",
                "María Fernández", "Pedro Sánchez", "Laura Martínez",
                "José López", "Carmen Díaz", "Miguel Torres"
        };
        return names[random.nextInt(names.length)];
    }

    private String getRandomTechnicianName() {
        String[] names = {
                "Roberto Silva", "Patricia Morales", "Antonio Vargas",
                "Isabel Castro", "Fernando Ruiz", "Claudia Herrera"
        };
        return names[random.nextInt(names.length)];
    }

    private String getRandomGeneralObservation() {
        String[] observations = {
                "Resultados dentro de los valores de referencia.",
                "Parámetros estables y satisfactorios.",
                "Valores compatibles con normalidad.",
                "Resultados apropiados para seguimiento."
        };
        return observations[random.nextInt(observations.length)];
    }

    private String getRandomGeneralConclusion() {
        String[] conclusions = {
                "Examen sin alteraciones significativas.",
                "Resultados satisfactorios.",
                "Parámetros dentro de límites normales.",
                "Estudio compatible con normalidad."
        };
        return conclusions[random.nextInt(conclusions.length)];
    }

    private String getRandomGeneralRecommendation() {
        String[] recommendations = {
                "Continuar seguimiento médico regular.",
                "Control periódico según criterio clínico.",
                "Mantener hábitos saludables.",
                "Repetir análisis según indicación médica."
        };
        return recommendations[random.nextInt(recommendations.length)];
    }

    // Métodos específicos para cardiología
    private String getRandomHeartRhythm() {
        String[] rhythms = {"Sinusal regular", "Sinusal con leve irregularidad", "Regular"};
        return rhythms[random.nextInt(rhythms.length)];
    }

    private String getRandomQRSAxis() {
        String[] axes = {"Normal (0° a +90°)", "Desviación izquierda leve", "Normal"};
        return axes[random.nextInt(axes.length)];
    }

    private String getRandomECGFinding(String type) {
        String[] findings = {
                "Sin alteraciones significativas",
                "Morfología normal",
                "Variante de la normalidad",
                "Dentro de límites normales"
        };
        return findings[random.nextInt(findings.length)];
    }

    private String getRandomECGInterpretation() {
        String[] interpretations = {
                "Electrocardiograma dentro de límites normales.",
                "Trazado compatible con normalidad.",
                "Sin evidencia de cardiopatía estructural.",
                "ECG normal para la edad del paciente."
        };
        return interpretations[random.nextInt(interpretations.length)];
    }

    private String getRandomECGConclusion() {
        String[] conclusions = {
                "Electrocardiograma normal.",
                "Sin alteraciones eléctricas significativas.",
                "Estudio cardiológico satisfactorio.",
                "ECG sin hallazgos patológicos."
        };
        return conclusions[random.nextInt(conclusions.length)];
    }

    private String getRandomECGRecommendations() {
        String[] recommendations = {
                "Control cardiológico de rutina.",
                "Seguimiento según evolución clínica.",
                "Mantener hábitos cardiosaludables.",
                "Control ECG anual."
        };
        return recommendations[random.nextInt(recommendations.length)];
    }

    // Métodos específicos para oncología
    private String getRandomOncologySample() {
        String[] samples = {
                "Biopsia por punción",
                "Muestra quirúrgica",
                "Citología",
                "Biopsia incisional"
        };
        return samples[random.nextInt(samples.length)];
    }

    private String getRandomMacroscopicDescription() {
        String[] descriptions = {
                "Fragmento de tejido de aspecto homogéneo.",
                "Muestra de consistencia firme y coloración normal.",
                "Tejido de características usuales para la región anatómica.",
                "Espécimen sin alteraciones macroscópicas evidentes."
        };
        return descriptions[random.nextInt(descriptions.length)];
    }

    private String getRandomMicroscopicDescription() {
        String[] descriptions = {
                "Arquitectura tisular preservada. Células de morfología normal.",
                "Tejido con características histológicas habituales.",
                "Patrón celular compatible con normalidad.",
                "Estructura microscópica sin alteraciones significativas."
        };
        return descriptions[random.nextInt(descriptions.length)];
    }

    private String getRandomSpecialTechniques() {
        String[] techniques = {
                "Inmunohistoquímica: Marcadores negativos para malignidad.",
                "Técnicas especiales: Sin aplicación necesaria.",
                "Coloraciones adicionales: Resultados normales.",
                "Estudios complementarios: No requeridos."
        };
        return techniques[random.nextInt(techniques.length)];
    }

    private String getRandomTumorMarkers() {
        String[] markers = {
                "No aplicable para este tipo de muestra.",
                "Marcadores dentro de rangos normales.",
                "Perfil inmunológico compatible con benignidad.",
                "Sin expresión de marcadores de malignidad."
        };
        return markers[random.nextInt(markers.length)];
    }

    private String getRandomOncologyDiagnosis() {
        String[] diagnoses = {
                "Tejido benigno sin evidencia de malignidad.",
                "Cambios reactivos benignos.",
                "Hiperplasia benigna.",
                "Proceso inflamatorio crónico."
        };
        return diagnoses[random.nextInt(diagnoses.length)];
    }

    private String getRandomTNMClassification() {
        return "No aplicable (lesión benigna).";
    }

    private String getRandomHistologicGrade() {
        return "No aplicable (proceso benigno).";
    }

    private String getRandomOncologyComments() {
        String[] comments = {
                "Los hallazgos son consistentes con proceso benigno.",
                "Se recomienda seguimiento clínico regular.",
                "Correlacionar con hallazgos clínicos e imagenológicos.",
                "Buen pronóstico con seguimiento adecuado."
        };
        return comments[random.nextInt(comments.length)];
    }

    private String getRandomOncologyRecommendations() {
        String[] recommendations = {
                "Control clínico e imagenológico periódico.",
                "Seguimiento oncológico de rutina.",
                "Correlación multidisciplinaria.",
                "Control según protocolo institucional."
        };
        return recommendations[random.nextInt(recommendations.length)];
    }
}