package com.ideas.springboot.app.auth.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas.springboot.app.auth.SimpleGrantedAuthorityMixin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * The Class JWTServiceImpli.
 * @author Israel Bejarano
 */
@Component
public class JWTServiceImpli implements JWTService {

	/**
	 * Creates the.
	 *
	 * @param auth the auth
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public String create(Authentication auth) throws IOException {
		String username = ((User) auth.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(SignatureAlgorithm.HS512, "Alguna.Clave.Secreta.123456".getBytes())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000*4L))
				.compact();
		
		return token;
	}

	/**
	 * Validate.
	 *
	 * @param token the token
	 * @return true, if successful
	 */
	@Override
	public boolean validate(String token) {
		
		try {
			getClaims(token);
			return true;
		} catch(JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Gets the claims.
	 *
	 * @param token the token
	 * @return the claims
	 */
	@Override
	public Claims getClaims(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey("Alguna.Clave.Secreta.123456".getBytes())
				.parseClaimsJws(resolve(token))
				.getBody();
		return claims;
	}

	/**
	 * Gets the username.
	 *
	 * @param token the token
	 * @return the username
	 */
	@Override
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	/**
	 * Gets the roles.
	 *
	 * @param token the token
	 * @return the roles
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get("authorities");
		Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
				.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
				.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
		
		return authorities;
	}

	/**
	 * Resolve.
	 *
	 * @param token the token
	 * @return the string
	 */
	@Override
	public String resolve(String token) {
		if(token != null && token.startsWith("Bearer ")) {
			return token.replace("Bearer ", "");			
		}
		return null;
	}
}