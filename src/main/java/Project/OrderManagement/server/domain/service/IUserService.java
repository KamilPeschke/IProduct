package Project.OrderManagement.server.domain.service;

import Project.OrderManagement.server.domain.entity.UserEntity;
import Project.OrderManagement.server.service.dto.*;
import org.apache.catalina.User;

import java.util.Optional;

public interface IUserService {

    UserEntity findUserById(IFindUserByIdDto findUserByIdDto);

    UserEntity registerUser(IRegisterUserDto registerUserDto);

    UserEntity updateUser(IUpdateUserDto updateUser);

    Boolean deleteUser(IDeleteUserDto deleteUserDto);

    Optional<UserEntity> loginUser(ILoginUserDto loginUserDto);
}
