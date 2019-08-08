package com.ideas.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ItemFactura.
 * @author Israel Bejarano
 */
@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The cantidad. */
	private Integer cantidad;
	
	@ManyToOne(fetch = FetchType.LAZY) // carga perezosa, evita que traiga todo de una sola vez, es lo recomendado
	@JoinColumn(name = "producto_id") // para generar la llave foreanea factura_id en la tabla facturas_items
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Producto producto;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the cantidad.
	 *
	 * @return the cantidad
	 */
	public Integer getCantidad() {
		return cantidad;
	}

	/**
	 * Sets the cantidad.
	 *
	 * @param cantidad the new cantidad
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	/**
	 * Gets the producto.
	 *
	 * @return the producto
	 */
	public Producto getProducto() {
		return producto;
	}

	/**
	 * Sets the producto.
	 *
	 * @param producto the new producto
	 */
	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	/**
	 * Calcular importe.
	 *
	 * @return the double
	 */
	public Double calcularImporte() {
		return cantidad.doubleValue() * producto.getPrecio();
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
}