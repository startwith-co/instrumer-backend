package instrumers.backend.user.vendor.repository;

import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.vendor.model.VendorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VendorRepositoryImpl implements VendorRepository {
    private final VendorJpaRepository vendorJpaRepository;

    public VendorEntity save(VendorEntity vendorEntity) {
        return vendorJpaRepository.save(vendorEntity);
    }

    @Override
    public Optional<VendorEntity> findByUserEntity(UserEntity userEntity) {
        return vendorJpaRepository.findByUserEntity(userEntity);
    }
}
