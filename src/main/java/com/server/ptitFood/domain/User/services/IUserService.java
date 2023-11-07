package com.server.ptitFood.domain.User.services;

import com.server.ptitFood.domain.User.exceptions.UserAlreadyExistException;
import com.server.ptitFood.domain.User.models.dto.RegisterDto;

public interface IUserService {
    public void register(RegisterDto registerDto)
            throws UserAlreadyExistException;

}
