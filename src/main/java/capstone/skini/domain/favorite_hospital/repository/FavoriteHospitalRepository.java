package capstone.skini.domain.favorite_hospital.repository;

import capstone.skini.domain.favorite_hospital.entity.FavoriteHospital;
import capstone.skini.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteHospitalRepository extends JpaRepository<FavoriteHospital, Long> {
    List<FavoriteHospital> findAllByUser(User user);
}
