package blps.lab2.service.topic;

import blps.lab2.model.domain.topic.Topic;
import blps.lab2.dao.TopicRepository;
import blps.lab2.model.domain.topic.TopicCategory;
import blps.lab2.model.requests.topic.CreateTopicRequest;
import blps.lab2.model.responses.topic.TopicView;
import blps.lab2.model.responses.topic.TopicViewPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    private final TopicRepository topicRepository;
    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    public Optional<Long> delete(long id) {
        if (topicRepository.existsById(id)) {
            topicRepository.deleteById(id);
            return Optional.of(id);
        }
        return Optional.empty();
    }

    public Topic update(long id, CreateTopicRequest req) {
        Optional<Topic> topicOptional = topicRepository.findById(id);
        if (topicOptional.isEmpty()) return null;

        Topic topic = topicOptional.get();
        topic.setTitle(req.getTitle());
        topic.setDescription(req.getDescription());
        topic.setContent(req.getContent());
        topic.setCategory(TopicCategory.valueOf(req.getCategory().toUpperCase()));
        topic.setUpdatedAt(new Date());
        return topicRepository.save(topic);
    }

    public Optional<Topic> findById(long id) {
        return topicRepository.findById(id);
    }

    public TopicViewPage findByQuery(
            String query,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir,
            Optional<TopicCategory> category
    ) {
        if (pageNumber < 0
                || pageSize < 0
                || !List.of("asc", "desc").contains(sortDir)
                || !List.of("id", "createdAt", "updatedAt").contains(sortBy)
        ) {
            throw new IllegalArgumentException();
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Topic> pageableTopics;
        if (category.isPresent()) {
            pageableTopics = topicRepository.findByCategoryAndQuery(category.get(), query, pageable);
        }
        else {
            pageableTopics = query.isEmpty()
                    ? topicRepository.findAll(pageable)
                    : topicRepository.findByQuery(query, pageable);
        }

        List<TopicView> topicViews = pageableTopics.getContent().stream().map(TopicView::fromTopic).toList();

        return new TopicViewPage(topicViews,
                pageableTopics.getNumber(),
                pageableTopics.getSize(),
                pageableTopics.getTotalElements(),
                pageableTopics.getTotalPages(),
                pageableTopics.isLast()
        );
    }
}
