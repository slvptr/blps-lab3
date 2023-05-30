package blps.lab2.dao;

import blps.lab2.model.domain.topic.Topic;
import blps.lab2.model.domain.topic.TopicCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByCategory(TopicCategory category);

    @Query("SELECT t FROM Topic t WHERE UPPER(t.title) LIKE UPPER(concat('%', :query, '%'))" +
            "OR UPPER(t.description) LIKE UPPER(concat('%', :query, '%'))" +
            "OR UPPER(t.content) LIKE UPPER(concat('%', :query, '%'))")
    Page<Topic> findByQuery(@Param("query") String query, Pageable pageable);

    @Query("SELECT t FROM Topic t WHERE t.category = :category AND (UPPER(t.title) LIKE UPPER(concat('%', :query, '%'))" +
            "OR UPPER(t.description) LIKE UPPER(concat('%', :query, '%'))" +
            "OR UPPER(t.content) LIKE UPPER(concat('%', :query, '%')))")
    Page<Topic> findByCategoryAndQuery(@Param("category") TopicCategory category, @Param("query") String query, Pageable pageable);

    Optional<Topic> findById(long id);
}
