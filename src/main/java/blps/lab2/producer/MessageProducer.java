package blps.lab2.producer;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;
import java.util.Date;

@Component
public class MessageProducer {
    private javax.jms.MessageProducer publisher;
    private Session session;

    @PostConstruct
    void init() throws JMSException {
        ConnectionFactory connectionFactory = new JmsConnectionFactory("amqp://localhost:5672");

        Connection connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("summary-tasks");
        publisher = session.createProducer(queue);

        connection.start();
    }

    public void sendGenerateSummaryMessage(String date) throws JMSException {
        publisher.send(session.createTextMessage(date));
    }
}
