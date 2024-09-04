package Project.OrderManagement.server.service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import Project.OrderManagement.server.NotFoundException;
import Project.OrderManagement.server.domain.repository.UserRepository;
import Project.OrderManagement.server.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import Project.OrderManagement.server.domain.entity.UserEntity;
import Project.OrderManagement.server.domain.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encodePassword;

    public UserEntity saveUser(UserEntity user){
        return userRepository.save(user);
    }

    @Override
    public UserEntity findUserById(IFindUserByIdDto findUserByIdDto){
        return userRepository.findById(findUserByIdDto.getId()).orElseThrow(() ->
                new NotFoundException(MessageFormat.format("User with id {0} does not exist", findUserByIdDto.getId())));
    }

    @Override
    public UserEntity registerUser(final IRegisterUserDto registerUserDto) {
        UserEntity user = new UserEntity();

        Optional<UserEntity> existingUserByUsername = userRepository.findByUsername(registerUserDto.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new RuntimeException("User already registered with this username");
        }
        user.setUsername(registerUserDto.getUsername());
        user.setPassword(encodePassword.encode(registerUserDto.getPassword()));
        user.setEmail(registerUserDto.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        return saveUser(user);
    }

    @Override
    public UserEntity updateUser(IUpdateUserDto updateUserDto) {
        UserEntity userEntity = userRepository.findById(updateUserDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateUserDto.getUsername() != null) {
            userEntity.setUsername(updateUserDto.getUsername());
        }
        if (updateUserDto.getPassword() != null) {
            userEntity.setPassword(encodePassword.encode(updateUserDto.getPassword()));
        }
        if (updateUserDto.getEmail() != null) {
            userEntity.setEmail(updateUserDto.getEmail());
        }
        userEntity.setUpdatedAt(LocalDateTime.now());

        return saveUser(userEntity);
    }


    @Override
    public Boolean deleteUser(IDeleteUserDto deleteUserDto) {
        Optional<UserEntity> user = userRepository.findById(deleteUserDto.getId());
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            userEntity.setDeletedAt(LocalDateTime.now());
            return true;
        }
            return false;
    }
    @Override
    public Optional<UserEntity> loginUser(ILoginUserDto loginUserDto) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(loginUserDto.getUsername());
        return userOptional.filter(userEntity ->
                encodePassword.matches(loginUserDto.getPassword(), userEntity.getPassword())
        );
    }
}
