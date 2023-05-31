package blps.lab2.dao;

import blps.lab2.model.domain.topic.Topic;
import blps.lab2.model.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {
    @Transactional
    @Modifying
    @Query("update User u set u.remainingGrades = 100")
    int updateRemainingGradesBy100();
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
}
