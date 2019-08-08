package com.ideas.springboot.app.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ideas.springboot.app.models.entity.Cliente;
import com.ideas.springboot.app.models.entity.Factura;
import com.ideas.springboot.app.models.entity.ItemFactura;
import com.ideas.springboot.app.models.entity.Producto;
import com.ideas.springboot.app.models.service.IClienteService;

// TODO: Auto-generated Javadoc
/**
 * The Class FacturaController.
 * @author Israel Bejarano
 */
@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	/** The cliente service. */
	@Autowired
	private IClienteService clienteService;
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/** The message source. */
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Ver.
	 *
	 * @param id the id
	 * @param model the model
	 * @param flash the flash
	 * @param locale the locale
	 * @return the string
	 */
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash, Locale locale) {
		// metodo sin optimizar la consulta (hace 7 consultas)
		// Factura factura = clienteService.findFacturaById(id);
		
		// metodo optimizado, hace una consulta con joins
		Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);
		if(factura == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", String.format(messageSource.getMessage("text.factura.ver.titulo", null, locale), factura.getDescripcion()));
		return "factura/ver";
	}

	/**
	 * Crear.
	 *
	 * @param clienteId the cliente id
	 * @param model the model
	 * @param flash the flash
	 * @param locale the locale
	 * @return the string
	 */
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		Cliente cliente = clienteService.findOne(clienteId);
		if(cliente == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		Factura factura = new Factura();
		factura.setCliente(cliente);
		model.put("factura", factura);
		model.put("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
		return "factura/form";
	}
	
	/**
	 * Cargar productos.
	 *
	 * @param term the term
	 * @return the list
	 */
	@GetMapping(value = "/cargar-productos/{term}", produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByNombre(term);
	}
	
	/**
	 * Guardar.
	 *
	 * @param factura the factura
	 * @param result the result
	 * @param model the model
	 * @param itemId the item id
	 * @param cantidad the cantidad
	 * @param flash the flash
	 * @param status the status
	 * @param locale the locale
	 * @return the string
	 */
	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model, @RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
			SessionStatus status, Locale locale) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
			return "factura/form";
		}
		if(itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
			model.addAttribute("error", messageSource.getMessage("text.factura.flash.lineas.error", null, locale));
			return "factura/form";
		}
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
		}
		
		clienteService.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.crear.success", null, locale));
		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	/**
	 * Eliminar.
	 *
	 * @param id the id
	 * @param flash the flash
	 * @param locale the locale
	 * @return the string
	 */
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {
		Factura factura= clienteService.findFacturaById(id);
		if(factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.eliminar.success", null, locale));
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		flash.addFlashAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
		return "redirect:/listar";
	}
}