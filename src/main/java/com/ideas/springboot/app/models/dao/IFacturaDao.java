package com.ideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ideas.springboot.app.models.entity.Factura;

/**
 * The Interface IFacturaDao.
 * @author Israel Bejarano
 */
public interface IFacturaDao extends CrudRepository<Factura, Long>{
	
	/**
	 * Fetch by id with cliente with item factura with producto.
	 *
	 * @param id the id
	 * @return the factura
	 */
	@Query("select f from Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id=?1")
	public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);

}