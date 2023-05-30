package blps.lab2.controller.enums;


import blps.lab2.model.domain.topic.TopicCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/enums")
public class EnumController {

    @GetMapping("/categories")
    public List<TopicCategory> getCategories() {
        return Arrays.asList(TopicCategory.values());
    }
}
