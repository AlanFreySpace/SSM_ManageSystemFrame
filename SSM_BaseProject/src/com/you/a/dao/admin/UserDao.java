package com.you.a.dao.admin;

import org.springframework.stereotype.Repository;

import com.you.a.entity.admin.User;

@Repository
public interface UserDao {
	public User findByUsername(String username);
}
