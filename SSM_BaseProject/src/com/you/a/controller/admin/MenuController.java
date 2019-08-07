package com.you.a.controller.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.admin.Menu;
import com.you.a.page.admin.Page;
import com.you.a.service.admin.MenuService;

@RequestMapping("admin/menu")
@Controller
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.addObject("topList", menuService.findTopList());
		model.setViewName("menu/list");
		return model;
	}
	
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getMenuList(Page page,
			@RequestParam(name="name",required = false,defaultValue = "") String name
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		queryMap.put("name", name);
		List<Menu> findList = menuService.findList(queryMap);
		ret.put("rows", findList);
		ret.put("total",menuService.getTotal(queryMap));
		return ret;
	}
	
	@RequestMapping(value = "/get_icons",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getMenuList(HttpServletRequest request){
		Map<String, Object> ret = new HashMap<String, Object>();
		String realPath = request.getServletContext().getRealPath("/");////获取SSM-BaseProject真实地址
		File file=new File(realPath+"\\resources\\admin\\easyui\\css\\icons");
		List<String> icons = new ArrayList<String>();
		if(!file.exists()) {
			ret.put("type", "error");
			ret.put("msg", "文件目录不存在");
			return ret;
		}
		File[] listFiles = file.listFiles();
		for(File f:listFiles) {
			if(f!=null && f.getName().contains("png")) {
				icons.add("icon-"+f.getName().substring(0,f.getName().indexOf(".")).replace("_", "-"));
			}
		}
		ret.put("type", "success");
		ret.put("content", icons);
		return ret;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Menu menu){
		Map<String, String> ret=new HashMap<String, String>();
		if(menu==null) {
			ret.put("type", "error");
			ret.put("msg", "请填写菜单信息!");
			return ret;
		}
		if(StringUtils.isEmpty(menu.getName())) {
			ret.put("type", "error");
			ret.put("msg", "请填写菜单名称!");
			return ret;
		}
		if(StringUtils.isEmpty(menu.getIcon())) {
			ret.put("type", "error");
			ret.put("msg", "请填写菜单图标!");
			return ret;
		}
		if(menu.getParentId()==null) {
			menu.setParentId(-1l);
		}
		if(menuService.add(menu)<=0) {
			ret.put("type", "error");
			ret.put("msg", "添加失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "添加成功!");
		return ret;
	}
}
