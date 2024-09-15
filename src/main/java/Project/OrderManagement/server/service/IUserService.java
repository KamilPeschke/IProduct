package Project.OrderManagement.server.service;

import Project.OrderManagement.server.dto.response.*;
import Project.OrderManagement.server.model.entity.UserEntity;

import java.util.Optional;

public interface IUserService {

    UserEntity registerUser(IRegisterUserDto registerUserDto);

    UserEntity updateUser(IUpdateUserDto updateUser, Long userId);

    Boolean deleteUser(IDeleteUserDto deleteUserDto);

    Optional<UserEntity> loginUser(ILoginUserDto loginUserDto);
}
