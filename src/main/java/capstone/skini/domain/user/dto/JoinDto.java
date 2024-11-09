package capstone.skini.domain.user.dto;

import capstone.skini.domain.user.entity.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinDto {
    private String username;
    private String loginId;
    private String password;
    private String email;
    private int age;
    private Gender gender;
}
