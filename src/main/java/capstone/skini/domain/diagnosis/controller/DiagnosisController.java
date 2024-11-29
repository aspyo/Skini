package capstone.skini.domain.diagnosis.controller;

import capstone.skini.domain.diagnosis.dto.DiagnosisDto;
import capstone.skini.domain.diagnosis.entity.DiagnosisType;
import capstone.skini.domain.diagnosis.service.DiagnosisService;
import capstone.skini.security.user.CustomPrincipal;
import com.amazonaws.services.s3.AmazonS3Client;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    /**
     * 피부 진단
     */
    @PostMapping(value = "/diagnosis", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doDiagnosis(@AuthenticationPrincipal CustomPrincipal principal,
                                         @RequestParam("type") DiagnosisType diagnosisType,
                                         @RequestParam("file") MultipartFile file) {
        if (principal == null) {
            return diagnosisService.diagnose(null, diagnosisType, file);
        }else{
            return diagnosisService.diagnose(principal.getLoginId(), diagnosisType, file);
        }
    }

    /**
     * 진단기록 조회
     */
    @GetMapping("/diagnosis")
    @Operation(summary = "진단기록 조회", description = "진단기록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "진단기록 조회 성공",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DiagnosisDto.class)))})
    })
    public ResponseEntity<?> findDiagnosis(@AuthenticationPrincipal CustomPrincipal principal) {
        String loginId = principal.getLoginId();
        return diagnosisService.findDiagnosis(loginId);
    }

    /**
     * 진단기록 삭제
     */
    @DeleteMapping("/diagnosis/{id}")
    public ResponseEntity<?> deleteDiagnosis(@AuthenticationPrincipal CustomPrincipal principal,
                                             @PathVariable("id") Long id) {
        String loginId = principal.getLoginId();
        return diagnosisService.deleteDiagnosis(id, loginId);
    }

}
