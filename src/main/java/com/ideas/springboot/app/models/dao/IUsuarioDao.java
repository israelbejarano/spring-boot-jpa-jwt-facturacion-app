package com.ideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.ideas.springboot.app.models.entity.Usuario;

/**
 * The Interface IUsuarioDao.
 * @author Israel Bejarano
 */
public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	
	/**
	 * Find by username.
	 *
	 * @param username the username
	 * @return the usuario
	 */
	public Usuario findByUsername(String username);

}