package Project.OrderManagement.server.configuration.security;

import Project.OrderManagement.server.model.entity.UserEntity;
import Project.OrderManagement.server.model.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.getUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " not found.");
        }

        UserEntity userEntity = userOptional.get();
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                new ArrayList<>()
        );
    }

}