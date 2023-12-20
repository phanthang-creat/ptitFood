package com.server.ptitFood.domain.services;

import com.server.ptitFood.common.helper.encoding.EncodingHelper;
import com.server.ptitFood.domain.dto.LoginDto;
import com.server.ptitFood.domain.entities.Admin;
import com.server.ptitFood.domain.exceptions.UsernameOrPasswordNotValid;
import com.server.ptitFood.domain.repositories.AdminRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AdminControlService {
    private final AdminRepository adminRepository;

    public AdminControlService(
            AdminRepository adminRepository
) {
        this.adminRepository = adminRepository;
}


    public Admin login(LoginDto loginDto) throws UsernameOrPasswordNotValid {
        if (adminRepository.findAdminByUserName(loginDto.getUsername()) == null) {
            throw new UsernameOrPasswordNotValid("Username or password not valid");
        }

        String encodedPassword = EncodingHelper.hashPassword(loginDto.getPassword());

        if (adminRepository.findAdminByUserNameAndPassword(loginDto.getUsername(), encodedPassword) == null) {

            throw new UsernameOrPasswordNotValid("Username or password not valid");
        }

        return adminRepository.findAdminByUserNameAndPassword(loginDto.getUsername(), encodedPassword);
    }

    public Admin getAdminById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Admin getAdminByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return adminRepository.findAdminByUserName(authentication.getName());
    }
}
