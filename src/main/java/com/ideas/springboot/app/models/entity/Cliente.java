package com.ideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The Class Cliente.
 * @author Israel Bejarano
 */
@Entity
@Table(name="clientes")
public class Cliente implements Serializable{

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The nombre. */
	@NotEmpty
	private String nombre;
	
	/** The apellido. */
	@NotEmpty
	private String apellido;
	
	/** The email. */
	@NotEmpty
	@Email
	private String email;
	
	/** The create at. */
	@NotNull
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	
	/** The foto. */
	private String foto;
	
	/** The facturas. */
	// carga perezosa (lazy), evita que traiga todo de una sola vez, es lo recomendado
	// persiste/elimina a sus elementos hijos en cascada de forma automatica
	// con mappedBy hacemos que sea bidireccional las acciones, crea el campo cliente id en la tabla facturas
	// orphanRemoval sirve para eliminar registros que se quedan colgando sin relacion ninguna
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cliente", orphanRemoval = true)
	@JsonManagedReference
	private List<Factura> facturas;
		
	/**
	 * Instantiates a new cliente.
	 */
	public Cliente() {
		facturas = new ArrayList<Factura>();
	}

	/**
	 * Gets the facturas.
	 *
	 * @return the facturas
	 */
	public List<Factura> getFacturas() {
		return facturas;
	}

	/**
	 * Sets the facturas.
	 *
	 * @param facturas the new facturas
	 */
	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	
	/**
	 * Adds the factura.
	 *
	 * @param factura the factura
	 */
	public void addFactura(Factura factura) {
		facturas.add(factura);
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
	 * Gets the apellido.
	 *
	 * @return the apellido
	 */
	public String getApellido() {
		return apellido;
	}

	/**
	 * Sets the apellido.
	 *
	 * @param apellido the new apellido
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the creates the at.
	 *
	 * @return the creates the at
	 */
	public Date getCreateAt() {
		return createAt;
	}

	/**
	 * Sets the creates the at.
	 *
	 * @param createAt the new creates the at
	 */
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the foto.
	 *
	 * @return the foto
	 */
	public String getFoto() {
		return foto;
	}

	/**
	 * Sets the foto.
	 *
	 * @param foto the new foto
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}