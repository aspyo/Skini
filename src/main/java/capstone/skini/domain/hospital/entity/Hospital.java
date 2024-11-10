package capstone.skini.domain.hospital.entity;

import capstone.skini.domain.common.BaseEntity;
import capstone.skini.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Hospital extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long id;

    private String hospitalName;
    private String address;
    private String phoneNumber;

    protected Hospital() {

    }
}
