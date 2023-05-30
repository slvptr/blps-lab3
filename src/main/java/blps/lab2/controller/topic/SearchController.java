package blps.lab2.controller.topic;


import blps.lab2.common.Constants;
import blps.lab2.model.domain.topic.TopicCategory;
import blps.lab2.model.responses.topic.TopicViewPage;
import blps.lab2.service.topic.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/search")
public class SearchController {
    private TopicService topicService;

    @Autowired
    public SearchController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/")
    public TopicViewPage searchAllByQuery(
            @RequestParam(value = "query", defaultValue = "", required = false) String query,
            @RequestParam(value = "category",  defaultValue = "", required = false) String category,
            @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        try {
            Optional<TopicCategory> optionalCategory;
            if (category.isEmpty()) {
                optionalCategory = Optional.empty();
            }
            else {
                optionalCategory = Optional.of(TopicCategory.valueOf(category.toUpperCase()));
            }
            return topicService.findByQuery(query, pageNumber, pageSize, sortBy, sortDir, optionalCategory);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
