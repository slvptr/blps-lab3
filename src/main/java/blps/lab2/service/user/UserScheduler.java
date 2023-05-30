package blps.lab2.service.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class UserScheduler {

    @Scheduled(cron = "0 * * ? * *")
    @Query("UPDATE User u SET u.remainingGrades = 10")
    public void updateLikes(){}
}
