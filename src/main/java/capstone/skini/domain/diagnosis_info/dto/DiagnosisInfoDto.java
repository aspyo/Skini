package capstone.skini.domain.diagnosis_info.dto;

import capstone.skini.domain.diagnosis.entity.DiagnosisType;
import capstone.skini.domain.diagnosis_info.entity.DiagnosisInfo;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DiagnosisInfoDto {
    private String engName;
    private String korName;
    private String description;
    private String guideline;

    public DiagnosisInfoDto(DiagnosisInfo diagnosisInfo) {
        engName = diagnosisInfo.getEngName();
        korName = diagnosisInfo.getKorName();
        description = diagnosisInfo.getDescription();
        guideline = diagnosisInfo.getGuideline();
    }
}
