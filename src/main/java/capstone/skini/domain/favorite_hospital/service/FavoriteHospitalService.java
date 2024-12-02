package capstone.skini.domain.favorite_hospital.service;

import capstone.skini.domain.favorite_hospital.dto.HospitalDto;
import capstone.skini.domain.favorite_hospital.entity.FavoriteHospital;
import capstone.skini.domain.favorite_hospital.repository.FavoriteHospitalRepository;
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
public class FavoriteHospitalService {

    private final FavoriteHospitalRepository favoriteHospitalRepository;
    private final UserService userService;

    public ResponseEntity<?> findFavorites(String loginId) {
        User findUser = userService.findByLoginId(loginId);
        if (findUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By loginId : " + loginId);
        }
        List<FavoriteHospital> allByUser = favoriteHospitalRepository.findAllByUser(findUser);
        List<HospitalDto> hospitalDtos = allByUser.stream().map(h -> new HospitalDto(h)).collect(Collectors.toList());
        return ResponseEntity.ok(hospitalDtos);
    }

    public ResponseEntity<?> addFavorites(HospitalDto hospitalDto, String loginId) {
        User findUser = userService.findByLoginId(loginId);
        if (findUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By loginId : " + loginId);
        }

        FavoriteHospital favoriteHospital = FavoriteHospital.builder()
                .hospitalName(hospitalDto.getHospitalName())
                .address(hospitalDto.getAddress())
                .latitude(hospitalDto.getLatitude())
                .longitude(hospitalDto.getLongitude())
                .phoneNumber(hospitalDto.getPhoneNumber())
                .user(findUser)
                .build();
        FavoriteHospital saved = favoriteHospitalRepository.save(favoriteHospital);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteFavorites(Long favoritesId, String loginId) {
        try {
            User findUser = userService.findByLoginId(loginId);
            if (findUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By loginId : " + loginId);
            }
            FavoriteHospital favoriteHospital = favoriteHospitalRepository.findById(favoritesId).orElseThrow(() -> new EntityNotFoundException("cannot find FavoriteHospital By Id : " + favoritesId));
            if (favoriteHospital.getUser().getId() != findUser.getId()) {
                return ResponseEntity.badRequest().body("즐겨찾기 병원의 유저 id와 현재 유저의 id가 다릅니다.");
            }
            favoriteHospitalRepository.delete(favoriteHospital);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
