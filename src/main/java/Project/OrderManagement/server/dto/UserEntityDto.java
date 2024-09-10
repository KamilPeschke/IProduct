package Project.OrderManagement.server.dto;

import Project.OrderManagement.server.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEntityDto {

   private UserEntity user;
   private String token;
}
