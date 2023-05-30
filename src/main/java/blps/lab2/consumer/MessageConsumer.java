package blps.lab2.consumer;

import blps.lab2.service.topic.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import javax.jms.JMSException;


@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    private final TopicService topicService;

    @Autowired
    public MessageConsumer(TopicService topicService) {
        this.topicService = topicService;
    }

    @JmsListener(destination = "summary-tasks")
    public void getSummaryRequest(String date) throws JMSException {
        LOGGER.info("Got summary request: since={}", date);

        topicService.generateSummary(Long.parseLong(date));
    }
}
