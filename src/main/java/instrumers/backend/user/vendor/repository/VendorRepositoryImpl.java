package instrumers.backend.user.vendor.repository;

import instrumers.backend.user.vendor.model.VendorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VendorRepositoryImpl implements VendorRepository {
    private final VendorJpaRepository vendorJpaRepository;

    public VendorEntity save(VendorEntity vendorEntity) {
        return vendorJpaRepository.save(vendorEntity);
    }
}
