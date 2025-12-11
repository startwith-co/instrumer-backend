package instrumers.backend.user.base.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.user.base.util.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@SQLDelete(sql = "UPDATE USER SET deleted = true WHERE user_seq = ?")
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

	@Builder.Default
	@Column(name = "deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean deleted = false;
}
