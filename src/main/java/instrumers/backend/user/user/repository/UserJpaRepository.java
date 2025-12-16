package instrumers.backend.user.user.repository;

import instrumers.backend.user.user.model.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.userSeq = :userSeq AND u.deleted = FALSE
            """)
    Optional<UserEntity> findByUserSeqLock(Long userSeq);
}
