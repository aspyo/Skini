package capstone.skini.domain.diagnosis.entity;

import capstone.skini.domain.common.BaseEntity;
import capstone.skini.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Diagnosis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DiagnosisType diagnosisType;

    private String result;
    private String confidenceScore;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Diagnosis() {

    }
}
