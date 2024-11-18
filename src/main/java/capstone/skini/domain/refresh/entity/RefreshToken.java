package capstone.skini.domain.refresh.entity;

import capstone.skini.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    private String username;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    private String expiration;

    protected RefreshToken() {

    }

}
