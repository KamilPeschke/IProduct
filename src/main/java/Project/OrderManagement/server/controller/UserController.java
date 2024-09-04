package Project.OrderManagement.server.controller;

import Project.OrderManagement.server.domain.entity.UserEntity;
import Project.OrderManagement.server.dto.UserEntityDto;
import Project.OrderManagement.server.request.RegisterUserRequest;
import Project.OrderManagement.server.security.JwtUtils;
import Project.OrderManagement.server.service.UserService;
import Project.OrderManagement.server.service.dto.IFindUserByIdDto;
import Project.OrderManagement.server.service.dto.ILoginUserDto;
import Project.OrderManagement.server.service.dto.IRegisterUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> findUserById(@RequestBody IFindUserByIdDto findUserByIdDto){
        UserEntity user = userService.findUserById(findUserByIdDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntityDto>registerUser(@RequestBody IRegisterUserDto registerUserDto){

        System.out.println("Registering user: " + registerUserDto.getUsername());

        String token = jwtUtils.generateTokenJwt(registerUserDto.getUsername());
        UserEntity user = userService.registerUser(registerUserDto);
        UserEntityDto response = new UserEntityDto(user,token);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?>loginUser(@RequestBody ILoginUserDto loginUserDto){
        System.out.println("login user: " + loginUserDto.getUsername());

        Optional<UserEntity> user = userService.loginUser(loginUserDto);

        if(user.isPresent()){
            UserEntity userEntity = user.get();
            String jwtToken = jwtUtils.generateTokenJwt(userEntity.getUsername());
            UserEntityDto response = new UserEntityDto(userEntity,jwtToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
