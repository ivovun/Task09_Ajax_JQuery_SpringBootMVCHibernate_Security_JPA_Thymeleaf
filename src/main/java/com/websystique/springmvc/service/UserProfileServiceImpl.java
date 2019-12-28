package com.websystique.springmvc.service;

import java.util.List;

import com.websystique.springmvc.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springmvc.model.UserProfile;


@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService{
	UserProfileRepository userProfileRepository;

	@Autowired
	public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
	}
	
	public UserProfile findById(long id) {
		return userProfileRepository.findById(id);
	}

	public UserProfile findByType(String type){
		return userProfileRepository.findByType(type);
	}

	public List<UserProfile> findAll() {
		return userProfileRepository.findAll();
	}
}
