package Project.OrderManagement.server.repository;

import Project.OrderManagement.server.domain.entity.UserEntity;
import Project.OrderManagement.server.domain.repository.IUserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository {

    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity save(UserEntity user) {
        Session session = sessionFactory.getCurrentSession();

        if(user.getId() == 0){
            session.persist(user);
        }else {
            user = (UserEntity) session.merge(user);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        UserEntity user = session.get(UserEntity.class, id);
        session.getTransaction().commit();

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        UserEntity user = session.createQuery("FROM UserEntity WHERE username = :username", UserEntity.class)
                .setParameter("username", username)
                .uniqueResult();
        session.getTransaction().commit();

        return Optional.ofNullable(user);
    }
}
