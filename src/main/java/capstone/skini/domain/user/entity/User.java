package capstone.skini.domain.user.entity;

import capstone.skini.domain.common.BaseEntity;
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
    private int age;
    private String role;

    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String location;

    protected User() {

    }
}
