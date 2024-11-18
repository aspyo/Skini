package capstone.skini.domain.user.dto;

import capstone.skini.domain.user.entity.Gender;
import capstone.skini.domain.user.entity.LoginType;
import capstone.skini.domain.user.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class UserDto {

    private String username;
    private String loginId;
    private String email;
    private Integer age;
    private Gender gender;
    private String location;

    public UserDto(User user) {
        username = user.getUsername();
        loginId = user.getLoginId();
        email = user.getEmail();
        age = user.getAge();
        gender = user.getGender();
        location = user.getLocation();
    }

}
