package capstone.skini.domain.user.dto;

import capstone.skini.domain.user.entity.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class EditUserDto {
    private String username;
    private String loginId;
    private String password;
    private String email;
    private Integer age;
    private Gender gender;
}
