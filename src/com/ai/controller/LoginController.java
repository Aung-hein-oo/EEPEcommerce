package com.ai.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.ai.model.User;
import com.ai.persistant.dao.UserDao;
import com.ai.persistant.dto.UserDto;

@Controller
public class LoginController {

	@Autowired
	private UserDao dao;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView showRegister(ModelMap model) {
		ModelAndView mav = new ModelAndView("/auth/login");
		mav.addObject("bean", new User());
		return mav;
	}

	@RequestMapping(value = "/LoginProcess", method = RequestMethod.POST)
	public String loginProcess(@ModelAttribute("bean") @Validated User user, BindingResult result, ModelMap model,
			HttpSession session) {

		if (result.hasFieldErrors("email") || result.hasFieldErrors("password")) {
			model.addAttribute("error", "Email and Password required!");
			return "redirect:/auth/login";
		}

		if (!user.getEmail().endsWith("@gmail.com")) {
			model.addAttribute("error", "Email should contain @gmail.com!");
			return "redirect:/auth/login";
		}

		
		UserDto dto = dao.getLogin(user);

		if (dto == null) {
			model.addAttribute("error", "Invalid Email or Password!");
			return "redirect:/auth/login";
		}

		
		session.setAttribute("email", user.getEmail());

		if (user.getEmail().equals("jondoe@gmail.com") && user.getPassword().equals("admin123")) {
			return "redirect:/list";
		} else {
			return "/frontend/home";
		}
	}
}
