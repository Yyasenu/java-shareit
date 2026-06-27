package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User mapToUser(UserDto userDto);

    UserDto mapToUserDto(User user);
}