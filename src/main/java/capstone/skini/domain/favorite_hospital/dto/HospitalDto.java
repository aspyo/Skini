package capstone.skini.domain.favorite_hospital.dto;

import capstone.skini.domain.favorite_hospital.entity.FavoriteHospital;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HospitalDto {
    private Long id;
    private String hospitalName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;

    public HospitalDto(FavoriteHospital favoriteHospital) {
        id = favoriteHospital.getId();
        hospitalName = favoriteHospital.getHospitalName();
        address = favoriteHospital.getAddress();
        latitude = favoriteHospital.getLatitude();
        longitude = favoriteHospital.getLongitude();
        phoneNumber = favoriteHospital.getPhoneNumber();
    }
}
