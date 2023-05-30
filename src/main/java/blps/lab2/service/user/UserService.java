package blps.lab2.service.user;

import blps.lab2.controller.exceptions.AlreadyExistException;
import blps.lab2.controller.exceptions.InvalidDataException;
import blps.lab2.dao.UserRepository;
import blps.lab2.model.domain.topic.Topic;
import blps.lab2.model.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        Optional<User> existedUser = getUserByEmail(user.getEmail());
        if (existedUser.isPresent()) throw new AlreadyExistException("User with such an email already exist");

        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
