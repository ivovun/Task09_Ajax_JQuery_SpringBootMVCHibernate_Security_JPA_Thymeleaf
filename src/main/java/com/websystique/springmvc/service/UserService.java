package com.websystique.springmvc.service;

import java.util.List;

import com.websystique.springmvc.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	List<User> findAll();

	User findById(long id);
	
	User findBySsoId(String ssoId);
	
	void save(User user);

	void deleteUserBySsoId(String sso);

	void updateUser(User user);

	boolean isUserSSOUnique(Long id, String sso);
}