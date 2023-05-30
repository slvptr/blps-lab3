package blps.lab2.model.requests.topic;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateTopicRequest implements Serializable {
    private String title;
    private String description;
    private String content;
    private String category;
}
