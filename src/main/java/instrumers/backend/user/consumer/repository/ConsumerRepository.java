package instrumers.backend.user.consumer.repository;

import instrumers.backend.user.consumer.model.ConsumerEntity;
import instrumers.backend.user.user.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ConsumerRepository {
    ConsumerEntity save(ConsumerEntity consumerEntity);

    Optional<ConsumerEntity> findByUserEntity(UserEntity userEntity);
}
