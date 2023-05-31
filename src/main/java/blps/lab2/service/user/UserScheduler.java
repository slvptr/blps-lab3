package blps.lab2.service.user;

import blps.lab2.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class UserScheduler {

    private final UserRepository userRepository;

    public UserScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void updateLikes(){
        log.info("Cron Cron Cron");
        userRepository.updateRemainingGradesBy100();
        userRepository.saveAll(userRepository.findAll());
    }
}
