package capstone.skini.domain.diagnosis.dto;

import capstone.skini.domain.diagnosis.entity.Diagnosis;
import capstone.skini.domain.diagnosis.entity.DiagnosisType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagnosisDto {
    private Long id;
    private String username;
    private DiagnosisType diagnosisType;
    private Boolean isPositive;
    private String result;
    private String confidenceScore;
    private String imageUrl;

    public DiagnosisDto(Diagnosis diagnosis) {
        id = diagnosis.getId();
        if (diagnosis.getUser() == null) {
            username = null;
        } else {
            username = diagnosis.getUser().getUsername();
        }
        diagnosisType = diagnosis.getDiagnosisType();
        isPositive = diagnosis.getIsPositive();
        result = diagnosis.getResult();
        confidenceScore = diagnosis.getConfidenceScore();
        imageUrl = diagnosis.getImageUrl();
    }
}
