package capstone.skini.domain.diagnosis_info.Controller;

import capstone.skini.domain.diagnosis_info.dto.DiagnosisInfoDto;
import capstone.skini.domain.diagnosis_info.entity.DiagnosisInfo;
import capstone.skini.domain.diagnosis_info.repository.DiagnosisInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiagnosisInfoController {

    private final DiagnosisInfoRepository diagnosisInfoRepository;

    @GetMapping("/diagnosis_info")
    public ResponseEntity<?> getDiagnosisInfo(@RequestParam("name") String name) {
        DiagnosisInfo diagnosisInfo = diagnosisInfoRepository.findByEngName(name);
        if (diagnosisInfo == null) {
            return ResponseEntity.badRequest().body("잘못된 질병 이름입니다.");
        }
        return ResponseEntity.ok(new DiagnosisInfoDto(diagnosisInfo));
    }

}
