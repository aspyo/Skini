package capstone.skini.domain.favorite_hospital.entity;

import capstone.skini.domain.common.BaseEntity;
import capstone.skini.domain.post.entity.Post;
import capstone.skini.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class FavoriteHospital extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_hospital_id")
    private Long id;

    private String hospitalName;
    private String address;
    private String point;
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected FavoriteHospital() {

    }
}
