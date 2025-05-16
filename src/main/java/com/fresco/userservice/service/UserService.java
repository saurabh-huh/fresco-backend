package com.fresco.userservice.service;

import com.fresco.userservice.model.dto.UserDto;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    UserDto loginUser(UserDto userDto);
}
