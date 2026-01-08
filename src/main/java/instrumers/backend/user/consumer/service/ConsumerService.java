package instrumers.backend.user.consumer.service;

import instrumers.backend.exception.ConflictException;
import instrumers.backend.exception.ServerException;
import instrumers.backend.user.consumer.repository.ConsumerRepository;
import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.user.repository.UserRepository;
import instrumers.backend.user.user.util.UserType;
import instrumers.backend.user.consumer.model.ConsumerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

import static instrumers.backend.user.consumer.controller.request.ConsumerRequest.*;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final UserRepository userRepository;
    private final ConsumerRepository consumerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void save(RegisterConsumerRequest request) {
        // Redis 분산 락 획득 시도
        String lockValue = UUID.randomUUID().toString();
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(
                "lock:register:" + request.email(),
                lockValue,
                Duration.ofSeconds(10)
        );

        if (!Boolean.TRUE.equals(lockAcquired)) {
            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "이미 처리 중인 요청입니다. 잠시 후 다시 시도해주세요."
            );
        }

        try {
            // 락 획득 후 중복 체크
            userRepository.findByEmail(request.email())
                    .ifPresent(user -> {
                        throw new ConflictException(
                                HttpStatus.CONFLICT.value(),
                                "이미 사용 중인 이메일입니다."
                        );
                    });

            UserEntity userEntity = UserEntity.builder()
                    .email(request.email())
                    .password(bCryptPasswordEncoder.encode(request.password()))
                    .userType(UserType.CONSUMER)
                    .build();
            UserEntity savedUserEntity = userRepository.save(userEntity);

            ConsumerEntity consumerEntity = ConsumerEntity.builder()
                    .businessName(request.businessName())
                    .managerName(request.managerName())
                    .phone(request.phone())
                    .userEntity(savedUserEntity)
                    .build();
            consumerRepository.save(consumerEntity);
        } catch (ConflictException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "이미 사용 중인 이메일입니다."
            );
        } catch (Exception e) {
            throw new ServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage()
            );
        } finally {
            // 락 값 확인 후 삭제 (본인이 획득한 락만 해제)
            String lockKey = "lock:register:" + request.email();
            String currentLockValue = redisTemplate.opsForValue().get(lockKey);
            if (lockValue.equals(currentLockValue)) {
                redisTemplate.delete(lockKey);
            }
        }
    }
}
