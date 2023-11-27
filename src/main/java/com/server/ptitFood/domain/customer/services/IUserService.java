package com.server.ptitFood.domain.customer.services;

import com.server.ptitFood.domain.exceptions.UserAlreadyExistException;
import com.server.ptitFood.domain.dto.customer.RegisterDto;

public interface IUserService {
    public void register(RegisterDto registerDto)
            throws UserAlreadyExistException;

}
