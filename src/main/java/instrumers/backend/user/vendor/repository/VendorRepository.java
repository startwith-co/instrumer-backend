package instrumers.backend.user.vendor.repository;

import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.vendor.model.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, Long> {
    Optional<VendorEntity> findByUserEntity(UserEntity userEntity);
    List<VendorEntity> findAllByUserEntityIn(List<UserEntity> userEntities);
}
