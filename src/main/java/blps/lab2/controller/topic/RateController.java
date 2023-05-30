package blps.lab2.controller.topic;

import blps.lab2.model.domain.topic.Topic;
import blps.lab2.model.domain.user.User;
import blps.lab2.model.responses.topic.TopicView;
import blps.lab2.service.rate.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/rate")
public class RateController {
    private final RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping("/{id}")
    public TopicView rateTopic(
            @PathVariable String id) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Topic topic = rateService.rateTopic(user.getId(), Long.parseLong(id));
            return TopicView.fromTopic(topic);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
