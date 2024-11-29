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
    private String result;
    private String confidenceScore;
    private String imageUrl;

    public DiagnosisDto(Diagnosis diagnosis) {
        id = diagnosis.getId();
        username = diagnosis.getUser().getUsername();
        diagnosisType = diagnosis.getDiagnosisType();
        result = diagnosis.getResult();
        confidenceScore = diagnosis.getConfidenceScore();
        imageUrl = diagnosis.getImageUrl();
    }
}
