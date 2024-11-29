package capstone.skini.domain.diagnosis_info.repository;

import capstone.skini.domain.diagnosis_info.entity.DiagnosisInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisInfoRepository extends JpaRepository<DiagnosisInfo, Long> {
    DiagnosisInfo findByEngName(String engName);
}
