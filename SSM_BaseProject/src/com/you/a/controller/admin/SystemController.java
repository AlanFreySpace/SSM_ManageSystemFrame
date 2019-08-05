package com.you.a.controller.admin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.admin.User;
import com.you.a.service.admin.UserService;
import com.you.a.util.CpachaUtil;

@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	private UserService UserService;
	
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
	
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> loginAct(User user,String cpacha,HttpServletRequest request){
		Map<String, String> ret=new HashMap<String, String>();
		if(user==null) {
			ret.put("type", "error");
			ret.put("msg", "����д�û���Ϣ!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "����д�û���!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "����д����!");
			return ret;
		}
		if(StringUtils.isEmpty(cpacha)) {
			ret.put("type", "error");
			ret.put("msg", "����д��֤��!");
			return ret;
		}
		Object loginCpacha = request.getSession().getAttribute("loginCpacha");
		if(loginCpacha==null) {
			ret.put("type", "error");
			ret.put("msg", "�Ự��ʱ����ˢ��ҳ��!");
			return ret;
		}
		if(!cpacha.toUpperCase().equals(loginCpacha.toString().toUpperCase())) {
			ret.put("type", "error");
			ret.put("msg", "��֤�����!");
			return ret;
		}
		User findByUsername = UserService.findByUsername(user.getUsername());
		if(findByUsername==null) {
			ret.put("type", "error");
			ret.put("msg", "�û���������!");
			return ret;
		}
		if(!user.getPassword().equals(findByUsername.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "�������!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "��¼�ɹ�!");
		return ret;
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
		String generatorVCode=cpachaUtil.generatorVCode();
		request.getSession().setAttribute(cpachaType, generatorVCode);
		BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		try {
			ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
}
