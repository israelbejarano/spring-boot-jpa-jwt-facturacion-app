package com.ideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ideas.springboot.app.models.entity.Producto;

// TODO: Auto-generated Javadoc
/**
 * The Interface IProductoDao.
 * @author Israel Bejarano
 */
public interface IProductoDao extends CrudRepository<Producto, Long> {
	
	/**
	 * Find by nombre.
	 *
	 * @param term the term
	 * @return the list
	 */
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);
	
	/**
	 * Find by nombre like ignore case.
	 *
	 * @param term the term
	 * @return the list
	 */
	// usando la doc de Springframework de esta forma no hace falta hacer la query va por debajo
	// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details
	public List<Producto> findByNombreLikeIgnoreCase(String term); 
}