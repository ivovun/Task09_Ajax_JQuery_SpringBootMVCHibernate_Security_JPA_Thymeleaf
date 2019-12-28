package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    UserProfile findById(long id);

    UserProfile findByType(String type);
}
