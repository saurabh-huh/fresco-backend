package com.fresco.userservice.service;

import com.fresco.userservice.enums.Status;
import com.fresco.userservice.enums.UserRole;
import com.fresco.userservice.mapper.UserMapper;
import com.fresco.userservice.model.dao.UserDao;
import com.fresco.userservice.model.dto.UserDto;
import com.fresco.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        UserDao userDao = userMapper.toDao(userDto);
        userDao.setRole(UserRole.ADMIN);
        userDao.setVerified(false);
        return userMapper.toDto(userRepository.save(userDao));
    }
}
