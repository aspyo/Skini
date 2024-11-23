package capstone.skini.domain.diagnosis.controller;

import capstone.skini.domain.diagnosis.dto.DiagnosisDto;
import capstone.skini.domain.diagnosis.service.DiagnosisService;
import capstone.skini.domain.favorite_hospital.dto.HospitalDto;
import capstone.skini.security.user.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @DeleteMapping("/diagnosis/{id}")
    public ResponseEntity<?> deleteDiagnosis(@PathVariable("id") Long id) {
        return diagnosisService.deleteDiagnosis(id);
    }
}
