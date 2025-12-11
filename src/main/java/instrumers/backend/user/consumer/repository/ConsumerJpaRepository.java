package instrumers.backend.user.consumer.repository;

import instrumers.backend.user.consumer.model.ConsumerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerJpaRepository extends JpaRepository<ConsumerEntity, Long> {
}
