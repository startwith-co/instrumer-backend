package instrumers.backend.user.user.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.user.user.util.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "USER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE user_seq = ? AND version = ?")
@Where(clause = "deleted = false")
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, unique = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, unique = false)
    private UserType userType;

    @Column(name = "profile_image_url", nullable = true, unique = false, length = 1024)
    private String profileImageUrl;

    @Builder.Default
    @Column(name = "deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean deleted = false;

    @Version
    @Builder.Default
    @Column(name = "version", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer version = 0;

    public void update(String email, String encodedPassword, String profileImageUrl) {
        if (email != null) this.email = email;
        if (encodedPassword != null) this.password = encodedPassword;
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
    }
}
