package com.ideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideas.springboot.app.models.service.IClienteService;
import com.ideas.springboot.app.view.xml.ClienteList;


/**
 * The Class ClienteRestController. Clase para ver un controlador REST y asi ya devuelve siempre  json/xml
 * @author Israel Bejarano
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

	/** The cliente service. */
	@Autowired
	private IClienteService clienteService;
	
	/**
	 * Listar.
	 *
	 * @return the cliente list
	 */
	@GetMapping(value = "/listar")
	public ClienteList listar() {
		return new ClienteList(clienteService.findAll());
	}
}