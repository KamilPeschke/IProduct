package Project.OrderManagement.server.service.dto;

import Project.OrderManagement.server.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class IDeleteUserDto extends UserEntity {

    private long id;
}
