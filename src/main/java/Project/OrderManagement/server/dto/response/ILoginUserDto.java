package Project.OrderManagement.server.dto.response;

import Project.OrderManagement.server.model.entity.UserEntity;
import lombok.AllArgsConstructor;
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
