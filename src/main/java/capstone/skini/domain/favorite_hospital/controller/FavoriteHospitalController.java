package capstone.skini.domain.favorite_hospital.controller;

import capstone.skini.domain.favorite_hospital.dto.HospitalDto;
import capstone.skini.domain.favorite_hospital.service.FavoriteHospitalService;
import capstone.skini.domain.user.dto.UserDto;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.service.UserService;
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
public class FavoriteHospitalController {

    private final FavoriteHospitalService favoriteHospitalService;

    @GetMapping("/favorites/hospital")
    @Operation(summary = "즐겨찾기 병원 조회", description = "즐겨찾기 병원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "즐겨찾기 병원 조회 성공",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HospitalDto.class)))})
    })
    public ResponseEntity<?> findFavoritesHospital(@AuthenticationPrincipal CustomPrincipal principal) {
        String loginId = principal.getLoginId();
        return favoriteHospitalService.findFavorites(loginId);
    }

    @PostMapping("/favorites/hospital")
    public ResponseEntity<?> addFavoritesHospital(@AuthenticationPrincipal CustomPrincipal principal,
                                                  @RequestBody HospitalDto hospitalDto) {
        String loginId = principal.getLoginId();
        return favoriteHospitalService.addFavorites(hospitalDto, loginId);
    }

    @DeleteMapping("/favorites/hospital/{id}")
    public ResponseEntity<?> deleteFavoritesHospital(@PathVariable("id") Long id) {
        return favoriteHospitalService.deleteFavorites(id);
    }

}
