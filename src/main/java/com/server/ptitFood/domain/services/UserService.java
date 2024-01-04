package com.server.ptitFood.domain.services;

import com.server.ptitFood.MailService.EmailServiceImpl;
import com.server.ptitFood.common.helper.CommonHelper;
import com.server.ptitFood.common.helper.encoding.EncodingHelper;
import com.server.ptitFood.domain.dto.*;
import com.server.ptitFood.domain.entities.OTP;
import com.server.ptitFood.domain.repositories.OtpRepository;
import com.server.ptitFood.domain.exceptions.UserAlreadyExistException;
import com.server.ptitFood.domain.exceptions.UsernameOrPasswordNotValid;
import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.repositories.UserRepository;
import com.server.ptitFood.domain.repositories.UserGroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

        boolean created = false;

        Customer user = userRepository.findCustomerByEmailOrUsername(registerDto.getEmail(), registerDto.getUsername()).orElse(null);

        if (user != null) {
            if (user.getStatus() == 0) {
                created = true;
            } else {
                throw new UserAlreadyExistException("User already exist");
            }
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

        if (created) {
            newUser.setId(user.getId());
        }

        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new UserAlreadyExistException(e.getMessage());
        }
        try {
            mailService.sendMessageUsingThymeleafTemplate(registerDto.getEmail(), "PTIT", templateModel);
        } catch (Exception e) {
            throw new UserAlreadyExistException("Email not valid");
        }

        OTP otp = serializerToOTP(registerDto.getEmail(), registerDto.getUsername(), otpCode);

        otpRepository.save(otp);

        return true;
    }

    public void reSendOtp(String email) throws UserAlreadyExistException {
        Customer user = userRepository.findCustomerByEmailOrUsername(email, "").orElse(null);
        if (user == null) {
            throw new UserAlreadyExistException("User not exist");
        }
        Map<String, String> props = new HashMap<>();

        Map<String, Object> templateModel = new HashMap<>();

        String otpCode = CommonHelper.generateOTP(6);

        props.put("otp", otpCode);

        templateModel.put("props", props);

        try {
            mailService.sendMessageUsingThymeleafTemplate(email, "PTIT", templateModel);
        } catch (Exception e) {
            throw new UserAlreadyExistException("Email not valid");
        }

        OTP otp = serializerToOTP(email, user.getUsername(), otpCode);

        otpRepository.save(otp);
    }

    private Customer serializerToUser(RegisterDto registerDto, String otp) {
        Customer user = new Customer();
        user.setFullName(registerDto.getFullName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(EncodingHelper.hashPassword(registerDto.getPassword()));
        user.setUserGroup(userGroupRepository.findUserGroupById(3));
        user.setStatus(0);
        return user;
    }

    private OTP serializerToOTP(String email, String username, String o) {
        OTP otp = new OTP();
        otp.setOtp(o);
        otp.setUserName(username);
        otp.setEmail(email);
        otp.setStatus(Boolean.FALSE);
        otp.setCreated(new Date());
        return otp;
    }

    public Customer login(LoginDto loginDto) throws UsernameOrPasswordNotValid {
        if (userRepository.findCustomerByUsername(loginDto.getUsername()).isEmpty()) {
            throw new UsernameOrPasswordNotValid("Username or password not valid");
        }

        String encodedPassword = EncodingHelper.hashPassword(loginDto.getPassword());

        if (userRepository.findUserByUsernameAndPassword(loginDto.getUsername(), encodedPassword).isEmpty()) {
            throw new UsernameOrPasswordNotValid("Username or password not valid");
        }

        return userRepository.findCustomerByUsername(loginDto.getUsername()).get();
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

        Customer user = userRepository.findCustomerByUsername(otp.getUserName()).isPresent() ?
                userRepository.findCustomerByUsername(otp.getUserName()).get() :
                null;

        if (user == null) {
            throw new UserAlreadyExistException("User not exist");
        }

        updateStatus(user.getUsername(), 1);
    }

    public Page<Customer> selectCustomerDecryption(Pageable pageable) {

        List<Customer> list = userRepository.findAll();

        System.out.println(list);

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
        return userRepository.findUserByUsernameAndStatus(username, 1).orElse(null);
    }

    public Customer getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserByUserName(authentication.getName());
    }

    @Transactional
    public Optional<Customer> getCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findCustomerByUsername(authentication.getName());
    }

    @Transactional
    public void update(ProfileDto profileDto) throws UsernameOrPasswordNotValid {
        if (profileDto.getFullname() == null || profileDto.getPhone() == null
                || profileDto.getFullname().isEmpty() || profileDto.getPhone().isEmpty()
        ) {
            throw new UsernameOrPasswordNotValid("Thông tin không hợp lệ");
        }
        Customer user = getCurrentUser();
        user.setFullName(profileDto.getFullname());
        user.setPhone(profileDto.getPhone());
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(UpdatePasswordDto updatePasswordDto) throws UsernameOrPasswordNotValid {
        System.out.println(updatePasswordDto);
        if (
                updatePasswordDto.getOldPassword() == null ||
                updatePasswordDto.getNewPassword() == null ||
                updatePasswordDto.getConfirmPassword() == null ||
                updatePasswordDto.getOldPassword().isEmpty() ||
                updatePasswordDto.getNewPassword().isEmpty() ||
                updatePasswordDto.getConfirmPassword().isEmpty()
        ) {
            throw new UsernameOrPasswordNotValid("Mật khẩu không hợp lệ");
        }
        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword())) {
            throw new UsernameOrPasswordNotValid("Mật khẩu mới không trùng khớp");
        }
        Customer user = getCurrentUser();
        if (!EncodingHelper.hashPassword(updatePasswordDto.getOldPassword()).equals(user.getPassword())) {
            throw new UsernameOrPasswordNotValid("Mật khẩu cũ không đúng");
        }
        user.setPassword(EncodingHelper.hashPassword(updatePasswordDto.getNewPassword()));
        userRepository.save(user);
    }
}
