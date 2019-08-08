package com.ideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The Class Producto.
 * @author Israel Bejarano
 */
@Entity
@Table(name = "productos")
public class Producto implements Serializable {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The nombre. */
	private String nombre;
	
	/** The precio. */
	private Double precio;
	
	/** The create at. */
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	
	@PrePersist
	public void prePersist() {
		createAt = new Date();
	}
	
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
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the nombre.
	 *
	 * @param nombre the new nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the precio.
	 *
	 * @return the precio
	 */
	public Double getPrecio() {
		return precio;
	}

	/**
	 * Sets the precio.
	 *
	 * @param precio the new precio
	 */
	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	/**
	 * Gets the create at.
	 *
	 * @return the create at
	 */
	public Date getCreateAt() {
		return createAt;
	}

	/**
	 * Sets the create at.
	 *
	 * @param createAt the new crate at
	 */
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
}