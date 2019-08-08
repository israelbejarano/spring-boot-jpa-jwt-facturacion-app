package com.ideas.springboot.app.auth.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas.springboot.app.auth.SimpleGrantedAuthoritiesMixin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

/**
 * The Class JWTAuthorizationFilter. Clase para validar el token
 * @author Israel Bejarano
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	/**
	 * Instantiates a new JWT authorization filter.
	 *
	 * @param authenticationManager the authentication manager
	 */
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
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
		
		String header = request.getHeader("Authorization");
		
		if(!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}
		
		boolean validoToken;
		Claims token = null;
		try {
			token = Jwts.parser()
			.setSigningKey("Alguna.Clave.Secreta.123456".getBytes())
			.parseClaimsJws(header.replace("Bearer ", ""))
			.getBody();
			validoToken = true;
		} catch(JwtException | IllegalArgumentException e) {
			validoToken = false;
		}
		
		UsernamePasswordAuthenticationToken authentication = null;
		if(validoToken) {
			String username = token.getSubject();
			Object roles = token.get("authorities");
			
			Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
					.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthoritiesMixin.class)
					.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
			authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
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
		if (header == null || !header.startsWith("Bearer ")) {
			return false;
		}
		return true;
	}
		
}