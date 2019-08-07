package com.you.a.service.admin;

import org.springframework.stereotype.Service;

import com.you.a.entity.admin.User;

@Service
public interface UserService {
	public User findByUsername(String username);
}
