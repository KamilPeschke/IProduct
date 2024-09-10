package Project.OrderManagement.server.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import Project.OrderManagement.server.exception.NotFoundException;
import Project.OrderManagement.server.dto.response.*;
import Project.OrderManagement.server.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import Project.OrderManagement.server.model.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Long getUserIdByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            UserEntity userEntity = user.get();
            return userEntity.getId();
        }else{
            throw new UsernameNotFoundException("This user does not exist");
        }
    }

    public Long getUserIdFromTokenJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByUsername(userDetails.getUsername())
                    .map(UserEntity::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("User is not authenticated");
    }

    @Override
    public UserEntity registerUser(final IRegisterUserDto registerUserDto) {
        UserEntity user = new UserEntity();

        Optional<UserEntity> existingUserByUsername = userRepository.findByUsername(registerUserDto.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new RuntimeException("User already registered with this username");
        }
        if (registerUserDto.getUsername() == null || registerUserDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if(registerUserDto.getPassword() == null || registerUserDto.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("Password is required");
        }

        user.setUsername(registerUserDto.getUsername().toLowerCase());
        user.setPassword(encodePassword.encode(registerUserDto.getPassword()));
        user.setEmail(registerUserDto.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        return saveUser(user);
    }

    @Override
    public Optional<UserEntity> loginUser(ILoginUserDto loginUserDto) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(loginUserDto.getUsername());
        return userOptional.filter(userEntity ->
                encodePassword.matches(loginUserDto.getPassword(), userEntity.getPassword())
        );
    }

    public UserEntity updateUser(IUpdateUserDto updateUserDto, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

}
