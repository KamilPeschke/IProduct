package Project.OrderManagement.server.dto.response;

import Project.OrderManagement.server.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class IRegisterUserDto extends UserEntity {

    @NotBlank(message ="username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "email is required")
    private String email;

    private LocalDateTime createdAt;

}
