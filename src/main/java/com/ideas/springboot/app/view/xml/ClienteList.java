package com.ideas.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ideas.springboot.app.models.entity.Cliente;

/**
 * The Class ClienteList. Clase wrapper (envoltorio) para la exportacion a xml
 * @author Israel Bejarano
 */
@XmlRootElement(name="clientes")
public class ClienteList {
	
	/** The clientes. */
	@XmlElement(name="cliente")
	public List<Cliente> clientes;

	/**
	 * Instantiates a new cliente list.
	 */
	public ClienteList() {}

	/**
	 * Instantiates a new cliente list.
	 *
	 * @param clientes the clientes
	 */
	public ClienteList(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	/**
	 * Gets the clientes.
	 *
	 * @return the clientes
	 */
	public List<Cliente> getClientes() {
		return clientes;
	}
}