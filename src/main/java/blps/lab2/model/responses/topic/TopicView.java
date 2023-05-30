package blps.lab2.model.responses.topic;

import blps.lab2.model.domain.topic.Topic;
import blps.lab2.model.domain.topic.TopicCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TopicView {
    private long id;
    private String title;
    private String description;
    private TopicCategory category;
    private Integer rate;
    private Date createdAt;

    public static TopicView fromTopic(Topic topic) {
        return new TopicView(topic.getId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getCategory(),
                topic.getRate(),
                topic.getCreatedAt()
        );
    }
}
