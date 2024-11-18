package capstone.skini.domain.favorite_hospital.controller;

import capstone.skini.domain.favorite_hospital.dto.HospitalDto;
import capstone.skini.domain.favorite_hospital.service.FavoriteHospitalService;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.service.UserService;
import capstone.skini.security.user.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteHospitalController {

    private final FavoriteHospitalService favoriteHospitalService;

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
