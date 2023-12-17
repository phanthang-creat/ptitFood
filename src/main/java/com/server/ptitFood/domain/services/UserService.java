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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

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

    public void register(RegisterDto registerDto) throws UserAlreadyExistException {

        if (checkUserExistInDBUser(registerDto.getEmail(), registerDto.getUsername())) {
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
            throw new UserAlreadyExistException("Username or email already exist");
        }

        try {
            mailService.sendMessageUsingThymeleafTemplate(registerDto.getEmail(), "PTIT", templateModel);
        } catch (Exception e) {
            throw new UserAlreadyExistException("Email not valid");
        }

        try {
            otpRepository.save(serializerToOTP(registerDto, otpCode));
        } catch (Exception e) {
            throw new UserAlreadyExistException("OTP not valid");
        }
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
        user.setOtp(otp);
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

        if (otp == null) {
            throw new UserAlreadyExistException("OTP not valid");
        }

        if (!otp.getOtp().equals(verifyEmailDto.getOtp())) {
            throw new UserAlreadyExistException("OTP not valid");
        }

        if (otp.getCreated().getTime() + 3 * 60 * 1000 < System.currentTimeMillis()) {
            throw new UserAlreadyExistException("OTP expired");
        }
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
}
