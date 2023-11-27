package com.server.ptitFood.domain.services;

import com.server.ptitFood.MailService.EmailServiceImpl;
import com.server.ptitFood.common.helper.CommonHelper;
import com.server.ptitFood.common.helper.encoding.EncodingHelper;
import com.server.ptitFood.domain.entities.OTP;
import com.server.ptitFood.domain.repositories.otp.OtpRepository;
import com.server.ptitFood.domain.exceptions.UserAlreadyExistException;
import com.server.ptitFood.domain.exceptions.UsernameOrPasswordNotValid;
import com.server.ptitFood.domain.dto.customer.LoginDto;
import com.server.ptitFood.domain.dto.customer.RegisterDto;
import com.server.ptitFood.domain.dto.customer.VerifyEmailDto;
import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.repositories.UserRepository;
import com.server.ptitFood.domain.repositories.userGroup.UserGroupRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        if (checkUserExistInDBOtp(registerDto)) {
            throw new UserAlreadyExistException("Please wait 3 minutes to resend OTP");
        }

        Map<String, String> props = new HashMap<>();
        Map<String, Object> templateModel = new HashMap<>();
        String otpCode = CommonHelper.generateOTP(6);
        props.put("otp", otpCode);
        templateModel.put("props", props);

        try {
            mailService.sendMessageUsingThymeleafTemplate(registerDto.getEmail(), "PTIT", templateModel);
        } catch (Exception e) {
            throw new UserAlreadyExistException("Email not valid");
        }

        OTP otp = serializerToOTP(registerDto);

        otp.setOtp(otpCode);

        otpRepository.save(otp);

    }

    private boolean checkUserExistInDBUser(String email, String username) {
        return userRepository.findUserByEmail(email).isPresent() ||
                userRepository.findUserByUserName(username).isPresent();
    }

    private boolean checkUserExistInDBOtp(RegisterDto registerDto) {
        OTP otp = otpRepository.findAll(
                Specification
                        .where(
                                OtpRepository.findByEmailOrUserName(
                                        registerDto.getEmail(), registerDto.getUsername()
                                )))
                .stream()
                .findFirst()
                .orElse(null);

        if (otp == null) {
            return false;
        }

        return otp.getCreated().getTime() + 3 * 60 * 1000 > System.currentTimeMillis();
    }

    private Customer serializerToUser(OTP otp) {


        Customer user = new Customer();
        user.setUserName(otp.getUserName());
        user.setPassword(otp.getPassword());
        user.setEmail(otp.getEmail());
        user.setFullName(otp.getFullName().trim().strip());
        user.setStatus(Boolean.TRUE);
        user.setTrash(Boolean.FALSE);
        user.setUserGroup(userGroupRepository.findUserGroupById(1));
        user.setCreated(new Date());
        user.setUpdated(new Date());

        return user;
    }

    private OTP serializerToOTP(RegisterDto registerDto) {
        OTP otp = new OTP();
        otp.setUserName(registerDto.getUsername());
        otp.setPassword(EncodingHelper.hashPassword(registerDto.getPassword()));
        otp.setEmail(registerDto.getEmail());
        otp.setFullName(registerDto.getFullName().trim().strip());
        otp.setStatus(Boolean.FALSE);
        otp.setCreated(new Date());
        otp.setUpdated(new Date());
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
        if (checkUserExistInDBUser(verifyEmailDto.getEmail(), "")) {
            throw new UserAlreadyExistException("Email or username already exist");
        }

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

        Customer user = serializerToUser(otp);

        userRepository.save(user);

        otpRepository.delete(otp);
    }
}
