package tfg.psygcv.medical.visit.command;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import tfg.psygcv.medical.visit.entity.VisitType;
import tfg.psygcv.pet.entity.ReproductiveStatus;
import tfg.psygcv.pet.entity.Temperament;

@Builder
@Getter
public class UpdateVisitCommand {

  private final LocalDate date;
  private final String reasonForVisit;
  private final VisitType visitType;
  private final String observations;
  private final ClinicalExamData clinicalExam;
  private final AnamnesisData anamnesis;
  private final List<DiagnosticData> diagnostics;
  private final List<TreatmentData> treatments;
  private final List<VaccineData> vaccines;

  @Builder
  @Getter
  public static class ClinicalExamData {

    private final Float temperature;
    private final Integer heartRate;
    private final Integer respiratoryRate;
    private final Float weight;
    private final Integer pulse;
    private final String mucosalMembranes;
    private final Temperament temperament;
    private final String description;
  }

  @Builder
  @Getter
  public static class AnamnesisData {

    private final String allergies;
    private final String previousDiseases;
    private final String surgeries;
    private final String currentMedications;
    private final String diet;
    private final ReproductiveStatus reproductiveStatus;
    private final LocalDate lastDewormingDate;
    private final LocalDate lastHeatDate;
    private final LocalDate lastBirthDate;
  }

  @Builder
  @Getter
  public static class DiagnosticData {

    private final Long id;

    private final List<String> problems;
  }

  @Builder
  @Getter
  public static class TreatmentData {

    private final Long id;

    private final String product;
    private final String route;
    private final String frequency;
    private final LocalDate startDate;
    private final LocalDate endDate;
  }

  @Builder
  @Getter
  public static class VaccineData {

    private final Long id;

    private final LocalDate applicationDate;
    private final String brand;
    private final String dose;
    private final String batch;
  }
}
