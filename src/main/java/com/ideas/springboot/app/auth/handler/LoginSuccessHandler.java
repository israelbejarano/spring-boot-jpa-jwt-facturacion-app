package com.ideas.springboot.app.auth.handler;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

/**
 * The Class LoginSuccessHandler. Clase que maneja el exito del login
 * @author Israel Bejarano
 */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	/** The message source. */
	@Autowired
	private MessageSource messageSource;
	
	/** The locale resolver. */
	@Autowired
    private LocaleResolver localeResolver;

	/**
	 * On authentication success.
	 *
	 * @param request the request
	 * @param response the response
	 * @param authentication the authentication
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		SessionFlashMapManager flashMapManager= new SessionFlashMapManager();
		FlashMap flashMap = new FlashMap();
		Locale locale = localeResolver.resolveLocale(request);
		String mensaje = String.format(messageSource.getMessage("text.login.success", null, locale), authentication.getName());
		
		flashMap.put("success", mensaje);
		flashMapManager.saveOutputFlashMap(flashMap, request, response);
		
		if(authentication != null) {
			logger.info(mensaje);
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
}