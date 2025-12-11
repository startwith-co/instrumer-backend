package instrumers.backend.user.vendor.repository;

import instrumers.backend.user.vendor.model.VendorEntity;
import org.springframework.stereotype.Component;

@Component
public interface VendorRepository {
    VendorEntity save(VendorEntity vendorEntity);
}
