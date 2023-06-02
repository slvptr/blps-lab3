package blps.lab2.service.user;

import blps.lab2.consumer.MessageConsumer;
import blps.lab2.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class UserScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    private final UserRepository userRepository;

    public UserScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 * * *")
    public void updateRemainingGrades(){
        LOGGER.info("Remaining grades have been updated");
        userRepository.updateRemainingGrades();
        userRepository.saveAll(userRepository.findAll());
    }
}
