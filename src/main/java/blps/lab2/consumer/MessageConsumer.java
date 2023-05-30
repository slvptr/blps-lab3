package blps.lab2.consumer;

import blps.lab2.model.requests.topic.CreateTopicRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @JmsListener(destination = "test-queue")
    public void messageListener(CreateTopicRequest systemMessage) {
        LOGGER.info("Message received! {}", systemMessage);
    }
}
