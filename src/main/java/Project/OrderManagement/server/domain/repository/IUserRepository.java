package Project.OrderManagement.server.domain.repository;
import Project.OrderManagement.server.domain.entity.UserEntity;
import java.util.Optional;


public interface IUserRepository {

    UserEntity save(UserEntity user);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUsername(String username);

}
