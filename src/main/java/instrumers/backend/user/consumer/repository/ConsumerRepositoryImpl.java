package instrumers.backend.user.consumer.repository;

import instrumers.backend.user.consumer.model.ConsumerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerRepositoryImpl implements ConsumerRepository {
    private final ConsumerJpaRepository consumerJpaRepository;

    @Override
    public ConsumerEntity save(ConsumerEntity consumerEntity) {
        return consumerJpaRepository.save(consumerEntity);
    }
}
