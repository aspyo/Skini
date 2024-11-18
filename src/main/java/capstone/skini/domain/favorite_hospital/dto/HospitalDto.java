package capstone.skini.domain.favorite_hospital.dto;

import lombok.Data;

@Data
public class HospitalDto {
    private String hospitalName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
}
