package Project.OrderManagement.server.service.dto;

import Project.OrderManagement.server.domain.entity.UserEntity;
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
