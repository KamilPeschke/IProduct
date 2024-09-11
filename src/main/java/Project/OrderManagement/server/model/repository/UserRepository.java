package Project.OrderManagement.server.model.repository;

import Project.OrderManagement.server.dto.response.IFindUserByIdDto;
import Project.OrderManagement.server.exception.NotFoundException;
import Project.OrderManagement.server.model.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public UserEntity saveUser(UserEntity user) {
        entityManager.persist(user);
        return user;
    }

    public UserEntity findUserById(Long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if (userEntity == null) {
            throw new NotFoundException(String.format("User with id %d does not exist", id));
        }
        return userEntity;
    }

    private UserEntity findUserByUsername(String username) {
            return entityManager.createQuery(
                            "SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        UserEntity userEntity = findUserByUsername(username);
        return Optional.of(userEntity);
    }

    public Long getUserIdByUsername(String username) {
        UserEntity userEntity = findUserByUsername(username);
        return userEntity.getId();
    }

    public Long getUserIdFromTokenJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Optional<UserEntity> userEntityOptional = entityManager.createQuery(
                            "SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                    .setParameter("username", userDetails.getUsername())
                    .getResultStream()
                    .findFirst();

            return userEntityOptional
                    .map(UserEntity::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("User is not authenticated");
    }
}
