package com.ideas.springboot.app.view.json;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.ideas.springboot.app.models.entity.Cliente;


/**
 * The Class ClienteListJsonView. Clase que maneja Json
 * @author Israel Bejarano
 */
@Component("listar.json")
@SuppressWarnings("unchecked")
public class ClienteListJsonView extends MappingJackson2JsonView {

	/**
	 * Filter model.
	 *
	 * @param model the model
	 * @return the object
	 */
	@Override
	protected Object filterModel(Map<String, Object> model) {
		model.remove("titulo");
		model.remove("page");
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		model.remove("clientes");
		model.put("clientes", clientes.getContent());	
		return super.filterModel(model);
	}
}