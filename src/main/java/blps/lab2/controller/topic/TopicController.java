package blps.lab2.controller.topic;

import blps.lab2.model.domain.topic.Topic;
import blps.lab2.model.domain.topic.TopicCategory;
import blps.lab2.model.domain.user.User;
import blps.lab2.model.requests.topic.CreateTopicRequest;
import blps.lab2.model.responses.topic.TopicView;
import blps.lab2.service.topic.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("api/v1/topic")
public class TopicController {
    private final TopicService topicService;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public TopicController(TopicService topicService, JmsTemplate jmsTemplate) {
        this.topicService = topicService;
        this.jmsTemplate = jmsTemplate;
    }

    @GetMapping("/{id}")
    public Topic getTopic(@PathVariable String id) {
        try {
            Optional<Topic> topic = topicService.findById(Long.parseLong(id));
            return topic.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public Long deleteTopic(@PathVariable String id) {
        try {
            Optional<Long> topicId = topicService.delete(Long.parseLong(id));
            return topicId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public Topic updateTopic(@PathVariable String id, @RequestBody CreateTopicRequest req) {
        try {
            Topic topic = topicService.update(Long.parseLong(id), req);
            if (topic == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return topic;
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/", consumes = "application/json")
    public TopicView createTopic(@RequestBody @Valid CreateTopicRequest req ) {
        try {
            jmsTemplate.convertAndSend("test-queue", req);
            Date currentDate = new Date();
            Topic topic = topicService.save(
                    new Topic(req.getTitle(),
                            req.getDescription(),
                            req.getContent(),
                            TopicCategory.valueOf(req.getCategory().toUpperCase()),
                            currentDate,
                            currentDate,
                            0,
                            (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                    ));
            return TopicView.fromTopic(topic);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no such topic category");
        }
    }
}