package blps.lab2.dao;

import blps.lab2.model.domain.topic.Topic;
import blps.lab2.model.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
}
