package instrumers.backend.user.vendor.service;

import instrumers.backend.exception.ConflictException;
import instrumers.backend.exception.NotFoundException;
import instrumers.backend.exception.ServerException;
import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.user.repository.UserRepository;
import instrumers.backend.user.user.util.UserType;
import instrumers.backend.user.vendor.model.VendorEntity;
import instrumers.backend.user.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static instrumers.backend.user.vendor.controller.request.VendorRequest.*;
import static instrumers.backend.user.vendor.controller.response.VendorResponse.*;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
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
                    .businessImageUrl(request.businessImageUrl())
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

    @Transactional
    public void update(Long userSeq, UpdateVendorRequest request) {
        try {
            UserEntity userEntity = userRepository.findById(userSeq)
                    .orElseThrow(() -> new NotFoundException(
                            HttpStatus.NOT_FOUND.value(),
                            "존재하지 않는 회원입니다."
                    ));

            String encodedPassword = request.password() != null ? bCryptPasswordEncoder.encode(request.password()) : null;
            userEntity.update(request.email(), encodedPassword, request.password());
            userRepository.save(userEntity);

            VendorEntity vendorEntity = vendorRepository.findByUserEntity(userEntity)
                    .orElseThrow(() -> new NotFoundException(
                            HttpStatus.NOT_FOUND.value(),
                            "존재하지 않는 기업 회원입니다."
                    ));
            vendorEntity.update(request.businessName(), request.phone(), request.bank(), request.account());
            vendorRepository.save(vendorEntity);
        } catch (OptimisticLockingFailureException e) {
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

    @Transactional(readOnly = true)
    public GetVendorResponse get(Long userSeq) {
        UserEntity userEntity = userRepository.findById(userSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 회원입니다."
                ));

        VendorEntity vendorEntity = vendorRepository.findByUserEntity(userEntity)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 기업 회원입니다."
                ));

        return new GetVendorResponse(
                userEntity.getUserSeq(), userEntity.getEmail(), userEntity.getUserType(),
                userEntity.getProfileImageUrl(), vendorEntity.getBusinessName(), vendorEntity.getManagerName(),
                vendorEntity.getPhone(), vendorEntity.getBank(), vendorEntity.getAccount()
        );
    }
}
