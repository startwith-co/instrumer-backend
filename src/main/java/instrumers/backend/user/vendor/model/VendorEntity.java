package instrumers.backend.user.vendor.model;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.user.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "VENDOR")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class VendorEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_seq")
    private Long vendorSeq;

    @Column(name = "business_name", nullable = false, unique = false)
    private String businessName;

    @Column(name = "manager_name", nullable = false, unique = false)
    private String managerName;

    @Column(name = "phone", nullable = false, unique = false)
    private String phone;

    @Column(name = "business_image_url", nullable = false, unique = false)
    private String businessImageUrl;

    @Column(name = "bank", nullable = true, unique = false)
    private String bank;

    @Column(name = "account", nullable = true, unique = false)
    private String account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false, unique = true)
    private UserEntity userEntity;

    @Version
    @Builder.Default
    @Column(name = "version", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer version = 0;

    public void update(String businessName, String phone, String bank, String account) {
        if (businessName != null) this.businessName = businessName;
        if (phone != null) this.phone = phone;
        if (bank != null) this.bank = bank;
        if (account != null) this.account = account;
    }
}
