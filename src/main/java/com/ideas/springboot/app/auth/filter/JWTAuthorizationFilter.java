package com.ideas.springboot.app.auth.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.ideas.springboot.app.auth.service.JWTService;
import com.ideas.springboot.app.auth.service.JWTServiceImpl;

/**
 * The Class JWTAuthorizationFilter. Clase para validar el token
 * @author Israel Bejarano
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private JWTService jWTService;

	/**
	 * Instantiates a new JWT authorization filter.
	 *
	 * @param authenticationManager the authentication manager
	 */
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jWTService) {
		super(authenticationManager);
		this.jWTService = jWTService;
	}

	/**
	 * Do filter internal.
	 *
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String header = request.getHeader(JWTServiceImpl.HEADER_STRING);
		
		if(!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}
		
		
		
		UsernamePasswordAuthenticationToken authentication = null;
		if(jWTService.validate(header)) {			
			authentication = new UsernamePasswordAuthenticationToken(jWTService.getUsername(header), null, jWTService.getRoles(header));
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
	
	/**
	 * Requires authentication.
	 *
	 * @param header the header
	 * @return true, if successful
	 */
	protected boolean requiresAuthentication(String header) {
		if (header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)) {
			return false;
		}
		return true;
	}
		
}