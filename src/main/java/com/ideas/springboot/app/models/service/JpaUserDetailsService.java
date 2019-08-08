package com.ideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ideas.springboot.app.models.dao.IUsuarioDao;
import com.ideas.springboot.app.models.entity.Role;
import com.ideas.springboot.app.models.entity.Usuario;

/**
 * The Class JpaUserDetailsService.
 * @author Israel Bejarano
 */
@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService{
	
	/** The usuario dao. */
	@Autowired
	private IUsuarioDao usuarioDao;
	
	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

	/**
	 * Load user by username.
	 *
	 * @param username the username
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDao.findByUsername(username);
		if(usuario == null) {
			logger.error("Error login: no existe el usuario '" + username + "'");
			throw new UsernameNotFoundException("Username " + username + " no existe en el sistema!");
		}
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(Role role: usuario.getRoles()) {
			logger.info("Role: ".concat(role.getAuthority()));
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		if(authorities.isEmpty()) {
			logger.error("Error login: usuario '" + username + "' no tiene roles asignados");
			throw new UsernameNotFoundException("Error login: usuario '" + username + "' no tiene roles asignados");
		}
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}
}