package com.ideas.springboot.app.auth.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ideas.springboot.app.models.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: Auto-generated Javadoc
/**
 * The Class JWTAuthenticationFilter.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	/** The authentication manager. */
	private AuthenticationManager authenticationManager;
	
	/**
	 * Instantiates a new JWT authentication filter.
	 *
	 * @param authenticationManager the authentication manager
	 */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
	}

	/**
	 * Attempt authentication.
	 *
	 * @param request the request
	 * @param response the response
	 * @return the authentication
	 * @throws AuthenticationException the authentication exception
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		if(username != null && password !=null) {
			logger.info("Username desde request parameter (form-data): " + username);
			logger.info("Password desde request parameter (form-data): " + password);
			
		} else {
			Usuario user = null;
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
				username = user.getUsername();
				password = user.getPassword();
				logger.info("Username desde request InputStream (raw): " + username);
				logger.info("Password desde request InputStream (raw): " + password);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		username = username.trim();
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authToken);
	}
	
	/**
	 * Successful authentication.
	 *
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @param authResult the auth result
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String username = ((User) authResult.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(SignatureAlgorithm.HS512, "Alguna.Clave.Secreta.123456".getBytes())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000*4L))
				.compact();
		
		response.addHeader("Authorization", "Bearer " + token);
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", token);
		body.put("user", (User) authResult.getPrincipal());
		body.put("mensaje", String.format("Hola %s, has iniciado sesión con éxito!", username));
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
	}
	
	/**
	 * Unsuccessful authentication.
	 *
	 * @param request the request
	 * @param response the response
	 * @param failed the failed
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("mensaje", "Error de autenticación: username o password incorrecto!");
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("application/json");
	}
}