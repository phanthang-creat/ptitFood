package com.server.ptitFood.domain.services;

import com.server.ptitFood.MailService.EmailServiceImpl;
import com.server.ptitFood.common.helper.CommonHelper;
import com.server.ptitFood.common.helper.encoding.EncodingHelper;
import com.server.ptitFood.domain.entities.OTP;
import com.server.ptitFood.domain.repositories.OtpRepository;
import com.server.ptitFood.domain.exceptions.UserAlreadyExistException;
import com.server.ptitFood.domain.exceptions.UsernameOrPasswordNotValid;
import com.server.ptitFood.domain.dto.LoginDto;
import com.server.ptitFood.domain.dto.RegisterDto;
import com.server.ptitFood.domain.dto.VerifyEmailDto;
import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.repositories.UserRepository;
import com.server.ptitFood.domain.repositories.UserGroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;

    private final EmailServiceImpl mailService;

    private final UserGroupRepository userGroupRepository;

    private final OtpRepository otpRepository;


    public UserService(
            UserRepository userRepository,
            UserGroupRepository userGroupRepository,
            EmailServiceImpl mailService,
            OtpRepository otpRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.mailService = mailService;
        this.otpRepository = otpRepository;
    }

    public boolean register(RegisterDto registerDto) throws UserAlreadyExistException {

        if (checkUserExistInDBUser(registerDto.getEmail(), registerDto.getUsername())) {
            System.out.println("Email or username already exist");
            throw new UserAlreadyExistException("Email or username already exist");
        }

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new UserAlreadyExistException("Password and confirm password not match");
        }

        Map<String, String> props = new HashMap<>();

        Map<String, Object> templateModel = new HashMap<>();

        String otpCode = CommonHelper.generateOTP(6);

        props.put("otp", otpCode);

        templateModel.put("props", props);

        Customer newUser = serializerToUser(registerDto, otpCode);

        try {
            userRepository.insertCustomerWithOtp(
                    newUser.getFullName(),
                    newUser.getUsername(),
                    newUser.getPassword(),
                    newUser.getEmail()
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new UserAlreadyExistException(e.getMessage());
        }
        try {
            mailService.sendMessageUsingThymeleafTemplate(registerDto.getEmail(), "PTIT", templateModel);
        } catch (Exception e) {
            throw new UserAlreadyExistException("Email not valid");
        }

        OTP otp = serializerToOTP(registerDto, otpCode);

        otpRepository.save(otp);

        return true;
    }

    private boolean checkUserExistInDBUser(String email, String username) {
        return userRepository.selectCustomerByEmailOrUsername(username, email).isPresent();
    }

    private Customer serializerToUser(RegisterDto registerDto, String otp) {
        Customer user = new Customer();
        user.setFullName(registerDto.getFullName());
        user.setUserName(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setUserGroup(userGroupRepository.findUserGroupById(3));
        return user;
    }

    private OTP serializerToOTP(RegisterDto registerDto, String o) {
        OTP otp = new OTP();
        otp.setOtp(o);
        otp.setUserName(registerDto.getUsername());
        otp.setEmail(registerDto.getEmail());
        otp.setStatus(Boolean.FALSE);
        otp.setCreated(new Date());
        return otp;
    }

    public Customer login(LoginDto loginDto) throws UsernameOrPasswordNotValid {
        if (userRepository.findUserByUserName(loginDto.getUsername()).isEmpty()) {
            throw new UsernameOrPasswordNotValid("Username or password not valid");
        }

        String encodedPassword = EncodingHelper.hashPassword(loginDto.getPassword());

        if (userRepository.findUserByUserNameAndPassword(loginDto.getUsername(), encodedPassword).isEmpty()) {

            throw new UsernameOrPasswordNotValid("Username or password not valid");
        }

        return userRepository.findUserByUserName(loginDto.getUsername()).get();
    }

    public void verify(VerifyEmailDto verifyEmailDto) throws UserAlreadyExistException {
        OTP otp = otpRepository.findAll(
                Specification
                        .where(
                                OtpRepository.findByEmailOrUserName(
                                        verifyEmailDto.getEmail(), ""
                                )))
                .stream()
                .findFirst()
                .orElse(null);

        System.out.println(otp);

        if (otp == null) {
            throw new UserAlreadyExistException("OTP not valid");
        }

        if (!otp.getOtp().equals(verifyEmailDto.getOtp())) {
            throw new UserAlreadyExistException("OTP not valid");
        }

        if (otp.getCreated().getTime() + 3 * 60 * 1000 < System.currentTimeMillis()) {
            throw new UserAlreadyExistException("OTP expired");
        }

        otp.setStatus(Boolean.TRUE);

        otpRepository.save(otp);

        Customer user = userRepository.findUserByUserName(otp.getUserName()).isPresent() ?
                userRepository.findUserByUserName(otp.getUserName()).get() :
                null;

        if (user == null) {
            throw new UserAlreadyExistException("User not exist");
        }

        updateStatus(user.getUsername(), 1);
    }

    public Page<Customer> selectCustomerDecryption(Pageable pageable) {

        List<Customer> list = userRepository.selectCustomerDecryption();

        return new PageImpl<>(list, pageable, list.size());
    }

    public Page<Customer> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<Customer> findByUsernameContaining(String username, Pageable pageable) {
        return userRepository.findByUsernameContaining(username, pageable);
    }

    public void updateStatus(String username, int status) {
        userRepository.updateStatus(status, username);
    }

    public Customer getUserById(Integer id) {
        return userRepository.findByIdAndStatus(id, 1);
    }

    public Customer getUserByUserName(String username) {
        return userRepository.findUserByUserNameAndStatus(username, 1).orElse(null);
    }

    public Customer getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserByUserName(authentication.getName());
    }

    @Transactional
    public Customer getCustomerDecryptionByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.selectCustomerDecryptionByUsername(authentication.getName());
    }
}
