package capstone.skini.domain.favorite_hospital.repository;

import capstone.skini.domain.favorite_hospital.entity.FavoriteHospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteHospitalRepository extends JpaRepository<FavoriteHospital, Long> {
}
