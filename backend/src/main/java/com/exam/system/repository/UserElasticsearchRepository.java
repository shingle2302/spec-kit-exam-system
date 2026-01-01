package com.exam.system.repository;

import com.exam.system.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserElasticsearchRepository extends ElasticsearchRepository<User, Long> {
    List<User> findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContaining(
        String username, String email, String firstName, String lastName);
    List<User> findByRole(String role);
}