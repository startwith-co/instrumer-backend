package instrumers.backend.user.vendor.service;

import instrumers.backend.exception.ConflictException;
import instrumers.backend.exception.ServerException;
import instrumers.backend.exception.code.ExceptionCodeMapper;
import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.user.repository.UserRepository;
import instrumers.backend.user.user.util.UserType;
import instrumers.backend.user.vendor.model.VendorEntity;
import instrumers.backend.user.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static instrumers.backend.exception.code.ExceptionCodeMapper.getCode;
import static instrumers.backend.user.vendor.controller.request.VendorRequest.*;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(RegisterVendorRequest request) {
        try {
            UserEntity userEntity = UserEntity.builder()
                    .email(request.email())
                    .password(bCryptPasswordEncoder.encode(request.password()))
                    .userType(UserType.VENDOR)
                    .build();
            UserEntity savedUserEntity = userRepository.save(userEntity);

            VendorEntity vendorEntity = VendorEntity.builder()
                    .businessName(request.businessName())
                    .managerName(request.managerName())
                    .phone(request.phone())
                    .businessImage(request.businessImage())
                    .userEntity(savedUserEntity)
                    .build();
            vendorRepository.save(vendorEntity);
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
