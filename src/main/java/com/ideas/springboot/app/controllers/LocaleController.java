package com.ideas.springboot.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The Class LocaleController.
 * @author Israel Bejarano
 */
@Controller
public class LocaleController {
	
	/**
	 * Locale.
	 *
	 * @param request the request
	 * @return the string
	 */
	@GetMapping("/locale")
	public String locale(HttpServletRequest request) {
		String ultimaUrl = request.getHeader("referer");
		return "redirect:".concat(ultimaUrl);
	}
}