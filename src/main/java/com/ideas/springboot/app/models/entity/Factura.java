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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The Class Factura.
 * @author Israel Bejarano
 */
@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The descripcion. */
	@NotEmpty
	private String descripcion;
	
	/** The observacion. */
	private String observacion;
	
	/** The create at. */
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	
	/** The cliente. */
	@ManyToOne(fetch = FetchType.LAZY) // carga perezosa, evita que traiga todo de una sola vez, es lo recomendado
	@JsonBackReference
	private Cliente cliente;
	
	/** The items. */
	// orphanRemoval sirve para eliminar registros que se quedan colgando sin relacion ninguna
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "factura_id") // para generar la llave foreanea factura_id en la tabla facturas_items
	private List<ItemFactura> items;
	
	/**
	 * Instantiates a new factura.
	 */
	public Factura() {
		this.items = new ArrayList<ItemFactura>();
	}

	/**
	 * Pre persist.
	 */
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
	 * Gets the descripcion.
	 *
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Sets the descripcion.
	 *
	 * @param descripcion the new descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Gets the observacion.
	 *
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * Sets the observacion.
	 *
	 * @param observacion the new observacion
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
	 * Gets the cliente.
	 *
	 * @return the cliente
	 */
	@XmlTransient	// para evitar bucles infinitos al exportar a xml
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Sets the cliente.
	 *
	 * @param cliente the new cliente
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public List<ItemFactura> getItems() {
		return items;
	}

	/**
	 * Sets the items.
	 *
	 * @param items the new items
	 */
	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	
	/**
	 * Adds the item factura.
	 *
	 * @param item the item
	 */
	public void addItemFactura(ItemFactura item) {
		this.items.add(item);
	}
	
	/**
	 * Gets the total.
	 *
	 * @return the total
	 */
	public Double getTotal() {
		Double total = 0.0;
		int size = items.size();
		for (int i = 0; i < size; i++) {
			total = items.get(i).calcularImporte();
		}
		return total;
	}
}