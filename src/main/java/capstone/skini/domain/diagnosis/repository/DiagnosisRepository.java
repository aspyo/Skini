package capstone.skini.domain.diagnosis.repository;

import capstone.skini.domain.diagnosis.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
}
