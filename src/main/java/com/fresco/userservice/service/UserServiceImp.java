package com.fresco.userservice.service;

import com.fresco.userservice.enums.Status;
import com.fresco.userservice.enums.UserRole;
import com.fresco.userservice.exceptionHandler.BusinessException;
import com.fresco.userservice.exceptionHandler.enums.ErrorCodes;
import com.fresco.userservice.mapper.UserMapper;
import com.fresco.userservice.model.dao.UserDao;
import com.fresco.userservice.model.dto.UserDto;
import com.fresco.userservice.repository.UserRepository;
import com.fresco.userservice.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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

    @Override
    public UserDto loginUser(UserDto userDto) {
        Optional<UserDao> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isPresent()) {
            UserDao userDao = user.get(); // Unwrap the Optional
            if (!userDao.getPassword().equals(userDto.getPassword())) {
                throw new BusinessException(ErrorCodes.INCORRECT_CREDENTIALS, HttpStatus.BAD_REQUEST);
            } else {
                String token = TokenUtil.generateToken(userDao.getEmail(), userDao.getRole().name(), userDao.isVerified());
                UserDto responseDto = userMapper.toDto(userDao); // Pass the unwrapped UserDao
                responseDto.setToken(token);
                return responseDto;
            }
        } else {
            throw new BusinessException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

    }

}
