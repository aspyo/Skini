package capstone.skini.domain.diagnosis.repository;

import capstone.skini.domain.diagnosis.entity.Diagnosis;
import capstone.skini.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findAllByUser(User user);
}
