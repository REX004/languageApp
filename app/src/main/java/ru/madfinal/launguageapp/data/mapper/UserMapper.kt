package ru.madfinal.launguageapp.data.mapper

import ru.madfinal.launguageapp.data.auth.models.UserDto
import ru.madfinal.launguageapp.domain.models.User

class UserMapper {
    fun mapToDomain(userDto: UserDto): User {
        return User(
            id = userDto.id,
            email = userDto.email,
            name = userDto.name
        )
    }
}