package Project.OrderManagement.server.model.repository;

import Project.OrderManagement.server.model.entity.ConfirmationToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ConfirmationTokenRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<ConfirmationToken> findEmailConfirmationToken(String token){
        try{
            ConfirmationToken confirmationToken = entityManager.createQuery(
                    "Select c FROM ConfirmationToken c WHERE c.token = :token", ConfirmationToken.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.of(confirmationToken);
        }catch (NoResultException e){
            return Optional.empty();
        }
    }
}
