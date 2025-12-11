package instrumers.backend.log.repository;

import instrumers.backend.log.domain.ExceptionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionLogEntityJpaRepository extends JpaRepository<ExceptionLogEntity, Long> {
}
