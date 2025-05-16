package com.fresco.userservice.mapper;

import com.fresco.userservice.model.dao.UserDao;
import com.fresco.userservice.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserDao userDao);
    UserDao toDao(UserDto userDto);
}
