package com.you.a.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.util.CpachaUtil;

@Controller
@RequestMapping("/system")
public class SystemController {
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model) {
		model.setViewName("system/index");
		model.addObject("name", "you");
		return model;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login(ModelAndView model) {
		model.setViewName("system/login");
		return model;
	}
	
	@RequestMapping(value="/get_cpacha",method=RequestMethod.GET)
	public void generateCpacha(
			@RequestParam(name="vl",required=false,defaultValue = "4") Integer vcodeLen,
			@RequestParam(name="w",required=false,defaultValue = "100") Integer width,
			@RequestParam(name="h",required=false,defaultValue = "30") Integer height,
			@RequestParam(name="type",required=true,defaultValue = "loginCpacha") String cpachaType,
			HttpServletRequest request,
			HttpServletResponse response) {
		CpachaUtil cpachaUtil=new CpachaUtil(vcodeLen, width, height);
	}
}
