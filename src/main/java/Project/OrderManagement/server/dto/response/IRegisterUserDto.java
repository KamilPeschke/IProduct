package Project.OrderManagement.server.dto.response;

import Project.OrderManagement.server.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class IRegisterUserDto extends UserEntity {

    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;

}
