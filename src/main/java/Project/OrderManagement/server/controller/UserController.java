package Project.OrderManagement.server.controller;

import Project.OrderManagement.server.dto.response.IUpdateUserDto;
import Project.OrderManagement.server.model.VerificationLinkStatus;
import Project.OrderManagement.server.model.entity.UserEntity;
import Project.OrderManagement.server.dto.UserEntityDto;
import Project.OrderManagement.server.configuration.security.JwtUtils;
import Project.OrderManagement.server.model.repository.UserRepository;
import Project.OrderManagement.server.service.email.EmailService;
import Project.OrderManagement.server.service.user.UserService;
import Project.OrderManagement.server.dto.response.ILoginUserDto;
import Project.OrderManagement.server.dto.response.IRegisterUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static Project.OrderManagement.server.model.VerificationLinkStatus.*;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> findUserById(@PathVariable Long id){
        UserEntity user = userRepository.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?>registerUser(@RequestBody IRegisterUserDto registerUserDto) {
        try {
            String token = jwtUtils.generateTokenJwt(registerUserDto.getUsername());
            UserEntity user = userService.registerUser(registerUserDto);
            UserEntityDto response = new UserEntityDto(user, token);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        }catch (IllegalArgumentException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        VerificationLinkStatus status = emailService.verifyEmail(token);

        switch (status) {
            case SUCCESS:
                return ResponseEntity.ok("Email verified successfully!");
            case TOKEN_EXPIRED:
                return ResponseEntity.status(HttpStatus.GONE).body("Verification token has expired.");
            case INVALID_TOKEN:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token.");
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?>loginUser(@RequestBody ILoginUserDto loginUserDto){

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

    @PostMapping("/update")
    public ResponseEntity<UserEntity> updateUser(@RequestBody IUpdateUserDto updateUserDto) {

        try{
            Long userId = userRepository.getUserIdFromTokenJwt();
            UserEntity updatedUser = userService.updateUser(updateUserDto, userId);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }catch (RuntimeException e){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/resend-email")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam("email") String email){
        try {
        emailService.resendEmailVerification(email);
             return ResponseEntity.ok("Email send successfully!");
         }catch (RuntimeException e){
             return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
         }
    }
}
