package capstone.skini.domain.diagnosis_info.Controller;

import capstone.skini.domain.diagnosis_info.dto.DiagnosisInfoDto;
import capstone.skini.domain.diagnosis_info.entity.DiagnosisInfo;
import capstone.skini.domain.diagnosis_info.repository.DiagnosisInfoRepository;
import capstone.skini.domain.post.dto.ResponsePostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "질병 정보 조회", description = "각 질병에 대한 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질병 정보 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DiagnosisInfoDto.class))})
    })
    public ResponseEntity<?> getDiagnosisInfo(@RequestParam("name") String name) {
        DiagnosisInfo diagnosisInfo = diagnosisInfoRepository.findByEngName(name);
        if (diagnosisInfo == null) {
            return ResponseEntity.badRequest().body("잘못된 질병 이름입니다.");
        }
        return ResponseEntity.ok(new DiagnosisInfoDto(diagnosisInfo));
    }

}
