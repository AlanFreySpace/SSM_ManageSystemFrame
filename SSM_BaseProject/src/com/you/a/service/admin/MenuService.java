package com.you.a.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.you.a.entity.admin.Menu;

@Service
public interface MenuService {
	public int add(Menu menu);
	public List<Menu> findList(Map<String, Object> queryMap);
	public List<Menu> findTopList();
	public int getTotal(Map<String, Object> queryMap);
}