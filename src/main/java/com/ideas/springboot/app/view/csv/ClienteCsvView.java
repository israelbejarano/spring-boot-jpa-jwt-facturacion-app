package com.ideas.springboot.app.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.ideas.springboot.app.models.entity.Cliente;

/**
 * The Class ClienteCsvView. Clase para exportar a CSV.
 * @author Israel Bejarano
 */
@Component("listar")
public class ClienteCsvView extends AbstractView {
	
	/**
	 * Instantiates a new cliente csv view.
	 */
	public ClienteCsvView() {
		setContentType("text/csv");
	}
	
	/**
	 * Generates download content.
	 *
	 * @return true, if successful
	 */
	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	/**
	 * Render merged output model.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");
		response.setContentType(getContentType());
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(),  CsvPreference.STANDARD_PREFERENCE);
		String[] header = {"id", "nombre", "apellido", "email", "createAt"};
		beanWriter.writeHeader(header);
		for(Cliente cliente: clientes) {
			beanWriter.write(cliente, header);
		}
		beanWriter.close();	
	}
}