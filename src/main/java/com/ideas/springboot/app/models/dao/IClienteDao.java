package com.ideas.springboot.app.models.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ideas.springboot.app.models.entity.Cliente;

/**
 * The Interface IClienteDao.
 * @author Israel Bejarano
 */
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{
	
	/**
	 * Fetch by id with facturas.
	 *
	 * @param id the id
	 * @return the cliente
	 */
	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchByIdWithFacturas(Long id);
	
}