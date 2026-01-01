package com.exam.system.repository;

import com.exam.system.model.Test;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestElasticsearchRepository extends ElasticsearchRepository<Test, Long> {
    List<Test> findByTitleContainingOrDescriptionContaining(String title, String description);
}