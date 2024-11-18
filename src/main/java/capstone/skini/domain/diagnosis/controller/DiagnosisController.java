package capstone.skini.domain.diagnosis.controller;

import capstone.skini.domain.diagnosis.service.DiagnosisService;
import capstone.skini.security.user.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @GetMapping("/diagnosis")
    public ResponseEntity<?> findDiagnosis(@AuthenticationPrincipal CustomPrincipal principal) {
        String loginId = principal.getLoginId();
        return diagnosisService.findDiagnosis(loginId);
    }

    @DeleteMapping("/diagnosis/{id}")
    public ResponseEntity<?> deleteDiagnosis(@PathVariable("id") Long id) {
        return diagnosisService.deleteDiagnosis(id);
    }
}
