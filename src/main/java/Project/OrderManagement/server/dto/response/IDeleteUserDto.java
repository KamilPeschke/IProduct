package Project.OrderManagement.server.dto.response;

import Project.OrderManagement.server.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class IDeleteUserDto extends UserEntity {

    private long id;
}
