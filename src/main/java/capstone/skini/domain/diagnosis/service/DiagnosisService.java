package capstone.skini.domain.diagnosis.service;

import capstone.skini.domain.diagnosis.dto.DiagnosisDto;
import capstone.skini.domain.diagnosis.entity.Diagnosis;
import capstone.skini.domain.diagnosis.entity.DiagnosisType;
import capstone.skini.domain.diagnosis.repository.DiagnosisRepository;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final UserService userService;

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

    public ResponseEntity<?> findDiagnosis(String loginId) {
        User findUser = userService.findByLoginId(loginId);
        if (findUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By loginId : " + loginId);
        }
        List<Diagnosis> allByUser = diagnosisRepository.findAllByUser(findUser);
        List<DiagnosisDto> diagnosisDtos = allByUser.stream().map(diagnosis -> new DiagnosisDto(diagnosis)).collect(Collectors.toList());
        return ResponseEntity.ok(diagnosisDtos);
    }

    public ResponseEntity<?> deleteDiagnosis(Long id) {
        try {
            Diagnosis diagnosis = diagnosisRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cannot find Diagnosis By Id: " + id));
            diagnosisRepository.delete(diagnosis);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
