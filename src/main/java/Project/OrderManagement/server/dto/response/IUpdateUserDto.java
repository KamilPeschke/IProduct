package Project.OrderManagement.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class IUpdateUserDto {
    private String username;
    private String password;
    private String email;
    private LocalDateTime updatedAt;

}
