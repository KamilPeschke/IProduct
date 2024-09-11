package Project.OrderManagement.server.service;

import java.time.LocalDateTime;
import java.util.Optional;

import Project.OrderManagement.server.dto.response.*;
import Project.OrderManagement.server.model.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import Project.OrderManagement.server.model.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encodePassword;


    @Override
    @Transactional
    public UserEntity registerUser(final IRegisterUserDto registerUserDto) {
        Optional<UserEntity> existingUserByUsername = userRepository.getUserByUsername(registerUserDto.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new RuntimeException("User already registered with this username");
        }
        if (registerUserDto.getUsername() == null || registerUserDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (registerUserDto.getPassword() == null || registerUserDto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerUserDto.getUsername().toLowerCase());
        user.setPassword(encodePassword.encode(registerUserDto.getPassword()));
        user.setEmail(registerUserDto.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.saveUser(user);
    }

        @Override
        public Optional<UserEntity> loginUser (ILoginUserDto loginUserDto){
            Optional<UserEntity> userOptional = userRepository.getUserByUsername(loginUserDto.getUsername());
            return userOptional.filter(userEntity ->
                    encodePassword.matches(loginUserDto.getPassword(), userEntity.getPassword())
            );
        }

        @Override
        @Transactional
        public UserEntity updateUser (IUpdateUserDto updateUserDto, Long userId){
            UserEntity userEntity = userRepository.findUserById(userId);

            if (updateUserDto.getUsername() != null && !updateUserDto.getUsername().trim().isEmpty()) {
                userEntity.setUsername(updateUserDto.getUsername());
            }
            if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().trim().isEmpty()) {
                userEntity.setEmail(updateUserDto.getEmail());
            }
            if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().trim().isEmpty()) {
                userEntity.setPassword(encodePassword.encode(updateUserDto.getPassword()));
            }

            userEntity.setUpdatedAt(LocalDateTime.now());

            return userRepository.saveUser(userEntity);
        }


    @Override
    @Transactional
    public Boolean deleteUser(IDeleteUserDto deleteUserDto) {
        UserEntity userEntity = userRepository.findUserById(deleteUserDto.getId());

        if (userEntity != null) {
            userEntity.setDeletedAt(LocalDateTime.now());
            entityManager.merge(userEntity);

            return true;
        }

        return false;
    }
}