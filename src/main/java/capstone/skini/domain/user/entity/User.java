package capstone.skini.domain.user.entity;

import capstone.skini.domain.common.BaseEntity;
import capstone.skini.domain.user.dto.EditUserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "Users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String loginId;
    private String password;
    private String email;
    private Integer age;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private String role;
    private String address;

    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    protected User() {

    }

    public void changeInfo(EditUserDto editUserDto) {
        username = editUserDto.getUsername();
        loginId = editUserDto.getLoginId();
        email = editUserDto.getEmail();
        age = editUserDto.getAge();
        gender = editUserDto.getGender();
    }
}
