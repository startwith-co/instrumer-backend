package instrumers.backend.user.consumer.model;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.user.user.model.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "CONSUMER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class ConsumerEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumer_seq")
    private Long consumerSeq;

    @Column(name = "business_name", nullable = false, unique = false)
    private String businessName;

    @Column(name = "manager_name", nullable = false, unique = false)
    private String managerName;

    @Column(name = "phone", nullable = false, unique = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserEntity userEntity;

    @Version
    @Builder.Default
    @Column(name = "version", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer version = 0;
}
