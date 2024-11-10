package capstone.skini.domain.diagnosis_info.entity;

import capstone.skini.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class DiagnosisInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_info")
    private Long id;

    private String diagnosisResult;
    private String description;
    private String guideline;

    protected DiagnosisInfo() {

    }

}

