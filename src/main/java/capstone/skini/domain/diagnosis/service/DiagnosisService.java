package capstone.skini.domain.diagnosis.service;

import capstone.skini.domain.diagnosis.entity.Diagnosis;
import capstone.skini.domain.diagnosis.entity.DiagnosisType;
import capstone.skini.domain.diagnosis.repository.DiagnosisRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    @Transactional(readOnly = true)
    public Diagnosis findById(Long id) {
        return diagnosisRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot Find Diagnosis By ID : " + id));
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> findAll() {
        return diagnosisRepository.findAll();
    }

    public Diagnosis createDiagnosis(Diagnosis diagnosis) {
        return diagnosisRepository.save(diagnosis);
    }

    public void deleteDiagnosis(Diagnosis diagnosis) {
        diagnosisRepository.delete(diagnosis);
    }
}
