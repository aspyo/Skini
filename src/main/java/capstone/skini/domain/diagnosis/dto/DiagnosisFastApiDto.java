package capstone.skini.domain.diagnosis.dto;

import capstone.skini.domain.diagnosis.entity.Diagnosis;
import capstone.skini.domain.diagnosis.entity.DiagnosisType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiagnosisFastApiDto {
    private Long id;
    private DiagnosisType diagnosisType;
    private Boolean isPositive;
    private String confidenceScore;
    private String diseaseName;
    private LocalDateTime createdAt;

    public DiagnosisFastApiDto(Diagnosis diagnosis) {
        id = diagnosis.getId();
        diagnosisType = diagnosis.getDiagnosisType();
        isPositive = diagnosis.getIsPositive();
        confidenceScore = diagnosis.getConfidenceScore();
        diseaseName = diagnosis.getResult();
        createdAt = diagnosis.getCreatedAt();
    }
}
