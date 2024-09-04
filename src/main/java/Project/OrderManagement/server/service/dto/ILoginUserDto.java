package Project.OrderManagement.server.service.dto;

import Project.OrderManagement.server.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ILoginUserDto extends UserEntity {

    private String username;
    private String password;
    private String email;

}
