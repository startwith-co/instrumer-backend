package instrumers.backend.user.consumer.repository;

import instrumers.backend.user.consumer.model.ConsumerEntity;
import org.springframework.stereotype.Component;

@Component
public interface ConsumerRepository {
    ConsumerEntity save(ConsumerEntity consumerEntity);
}
