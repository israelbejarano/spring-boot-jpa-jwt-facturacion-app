package com.ideas.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ideas.springboot.app.models.dao.IClienteDao;
import com.ideas.springboot.app.models.dao.IFacturaDao;
import com.ideas.springboot.app.models.dao.IProductoDao;
import com.ideas.springboot.app.models.entity.Cliente;
import com.ideas.springboot.app.models.entity.Factura;
import com.ideas.springboot.app.models.entity.Producto;

/**
 * The Class ClienteServiceImpl.
 * @author Israel Bejarano
 */
@Service
public class ClienteServiceImpl implements IClienteService {
	
	/** The cliente dao. */
	@Autowired
	private IClienteDao clienteDao;
	
	/** The producto dao. */
	@Autowired
	private IProductoDao productoDao;
	
	/** The factura dao. */
	@Autowired
	private IFacturaDao facturaDao;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}
	
	/**
	 * Find all.
	 *
	 * @param pageable the pageable
	 * @return the page
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}
	
	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the cliente
	 */
	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return clienteDao.findById(id).orElse(null);
	}
	
	/**
	 * Fetch by id with facturas.
	 *
	 * @param id the id
	 * @return the cliente
	 */
	@Override
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithFacturas(Long id) {
		return clienteDao.fetchByIdWithFacturas(id);
	}

	/**
	 * Save.
	 *
	 * @param cliente the cliente
	 */
	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}

	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	/**
	 * Find by nombre.
	 *
	 * @param term the term
	 * @return the list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		return productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}

	/**
	 * Save factura.
	 *
	 * @param factura the factura
	 */
	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
	}

	/**
	 * Find producto by id.
	 *
	 * @param id the id
	 * @return the producto
	 */
	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		return productoDao.findById(id).orElse(null);
	}

	/**
	 * Find factura by id.
	 *
	 * @param id the id
	 * @return the factura
	 */
	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return facturaDao.findById(id).orElse(null);
	}

	/**
	 * Delete factura.
	 *
	 * @param id the id
	 */
	@Override
	@Transactional
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);
	}

	/**
	 * Fetch factura by id with cliente with item factura with producto.
	 *
	 * @param id the id
	 * @return the factura
	 */
	@Override
	@Transactional(readOnly = true)
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
		return facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
	}
}