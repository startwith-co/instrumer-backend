package instrumers.backend.user.consumer.repository;

import instrumers.backend.user.consumer.model.ConsumerEntity;
import instrumers.backend.user.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConsumerRepositoryImpl implements ConsumerRepository {
    private final ConsumerJpaRepository consumerJpaRepository;

    @Override
    public ConsumerEntity save(ConsumerEntity consumerEntity) {
        return consumerJpaRepository.save(consumerEntity);
    }

    @Override
    public Optional<ConsumerEntity> findByUserEntity(UserEntity userEntity) {
        return consumerJpaRepository.findByUserEntity(userEntity);
    }
}
