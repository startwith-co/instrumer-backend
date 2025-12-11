package instrumers.backend.user.user.repository;

import instrumers.backend.user.user.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository {
    UserEntity save(UserEntity userEntity);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserSeq(Long userSeq);
}
