package instrumers.backend.user.vendor.repository;

import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.vendor.model.VendorEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface VendorRepository {
    VendorEntity save(VendorEntity vendorEntity);

    Optional<VendorEntity> findByUserEntity(UserEntity userEntity);
}
