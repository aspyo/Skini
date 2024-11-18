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

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteHospitalService {

    private final FavoriteHospitalRepository favoriteHospitalRepository;
    private final UserService userService;

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

    public ResponseEntity<?> deleteFavorites(Long id) {
        try {
            FavoriteHospital favoriteHospital = favoriteHospitalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cannot find FavoriteHospital By Id : " + id));
            favoriteHospitalRepository.delete(favoriteHospital);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
