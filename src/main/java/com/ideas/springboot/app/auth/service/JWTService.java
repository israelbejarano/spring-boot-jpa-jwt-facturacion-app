package com.ideas.springboot.app.auth.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;

/**
 * The Interface JWTService. Servicio para la gestión de jwt
 * @author Israel Bejarano
 */
public interface JWTService {
	
	/**
	 * Creates the.
	 *
	 * @param auth the auth
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String create(Authentication auth) throws IOException;
	
	/**
	 * Validate.
	 *
	 * @param token the token
	 * @return true, if successful
	 */
	public boolean validate(String token);
	
	/**
	 * Gets the claims.
	 *
	 * @param token the token
	 * @return the claims
	 */
	public Claims getClaims(String token);
	
	/**
	 * Gets the username.
	 *
	 * @param token the token
	 * @return the username
	 */
	public String getUsername(String token);
	
	/**
	 * Gets the roles.
	 *
	 * @param token the token
	 * @return the roles
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
	
	/**
	 * Resolve.
	 *
	 * @param token the token
	 * @return the string
	 */
	public String resolve(String token);
}