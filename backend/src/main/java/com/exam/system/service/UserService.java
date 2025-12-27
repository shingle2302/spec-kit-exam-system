package com.exam.system.service;

import com.exam.system.model.User;
import com.exam.system.repository.UserElasticsearchRepository;
import com.exam.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CacheService cacheService;
    
    @Autowired(required = false)
    private UserElasticsearchRepository userElasticsearchRepository;

    public User findByUsername(String username) {
        String cacheKey = "user_username_" + username;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (User) cached;
        }
        
        User user = userRepository.findByUsername(username);
        if (user != null) {
            cacheService.setEntity(cacheKey, user);
        }
        
        return user;
    }

    public User findById(String id) {
        String cacheKey = "user_id_" + id;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (User) cached;
        }
        
        User user = userRepository.findById(id);
        if (user != null) {
            cacheService.setEntity(cacheKey, user);
        }
        
        return user;
    }

    public User save(User user) {
        User result;
        if (user.getId() == null || userRepository.selectById(user.getId()) == null) {
            userRepository.insert(user);
            result = user;
        } else {
            userRepository.updateById(user);
            result = user;
        }
        
        // Also save to Elasticsearch if available
        if (userElasticsearchRepository != null) {
            try {
                userElasticsearchRepository.save(user);
            } catch (Exception e) {
                // Log the error but don't fail the operation
                System.err.println("Failed to save user to Elasticsearch: " + e.getMessage());
            }
        }
        
        // Clear related cache entries
        if (user.getUsername() != null) {
            cacheService.delete("user_username_" + user.getUsername());
        }
        if (user.getId() != null) {
            cacheService.delete("user_id_" + user.getId());
        }
        
        return result;
    }
    


    public java.util.List<User> searchUsers(String query) {
        if (userElasticsearchRepository != null) {
            try {
                return userElasticsearchRepository.findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContaining(
                    query, query, query, query);
            } catch (Exception e) {
                // Log the error but return empty list
                System.err.println("Failed to search users in Elasticsearch: " + e.getMessage());
                return java.util.Collections.emptyList();
            }
        } else {
            // Return empty list if Elasticsearch is not available
            return java.util.Collections.emptyList();
        }
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }
}