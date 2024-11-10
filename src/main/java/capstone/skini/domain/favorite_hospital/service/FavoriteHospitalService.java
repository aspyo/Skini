package capstone.skini.domain.favorite_hospital.service;

import capstone.skini.domain.favorite_hospital.repository.FavoriteHospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteHospitalService {

    private final FavoriteHospitalRepository favoriteHospitalRepository;

}
