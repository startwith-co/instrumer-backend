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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static instrumers.backend.user.consumer.controller.request.ConsumerRequest.*;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final UserRepository userRepository;
    private final ConsumerRepository consumerRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void save(RegisterConsumerRequest request) {
        try {
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
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    e.getMessage()
            );
        } catch (Exception e) {
            throw new ServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage()
            );
        }
    }

}
