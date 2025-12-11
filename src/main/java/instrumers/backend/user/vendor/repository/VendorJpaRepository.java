package instrumers.backend.user.vendor.repository;

import instrumers.backend.user.vendor.model.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorJpaRepository extends JpaRepository<VendorEntity, Long> {

}
