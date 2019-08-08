package com.ideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ideas.springboot.app.models.entity.Cliente;
import com.ideas.springboot.app.models.service.IClienteService;
import com.ideas.springboot.app.models.service.IUploadFileService;
import com.ideas.springboot.app.util.paginator.PageRender;
import com.ideas.springboot.app.view.xml.ClienteList;

/**
 * The Class ClienteController.
 * @author Israel Bejarano
 */
@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	
	/** The cliente service. */
	@Autowired
	private IClienteService clienteService;
	
	/** The upload file service. */
	@Autowired
	private IUploadFileService uploadFileService;
	
	/** The logger. */
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	/** The message source. */
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Ver foto.
	 *
	 * @param filename the filename
	 * @return the response entity
	 */
	@Secured("ROLE_USER")
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	/**
	 * Ver.
	 *
	 * @param id the id
	 * @param model the model
	 * @param flash the flash
	 * @param locale the locale
	 * @return the string
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		// metodo sin optimizar la forma de traer las facturas
		// Cliente cliente = clienteService.findOne(id);
		
		// metodo con optimizacion a la hora de traerse las facturas, hace una unica select con join
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);
		if(cliente == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ").concat(cliente.getNombre()));
		
		return "ver";
	}
	
	/**
	 * Listar rest. Devuelve el listado de clientes de Forma rest (json o xml)
	 *
	 * @return the list
	 */
	@GetMapping(value = "/listar-rest")
	public @ResponseBody ClienteList listarRest() {
	
		return new ClienteList(clienteService.findAll());
	}
	
	/**
	 * Listar.
	 *
	 * @param page the page
	 * @param model the model
	 * @param authentication the authentication
	 * @param request the request
	 * @param locale the locale
	 * @return the string
	 */
	@RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
						Authentication authentication, HttpServletRequest request, Locale locale) {
		
		if(authentication != null) {
			logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			logger.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Hola, tu username es: ".concat(auth.getName()));
		}
		
		if(hasRole("ROLE_ADMIN")) {
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso."));
		} else {
			logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso."));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if(securityContext.isUserInRole("ADMIN")) {
			logger.info("Hola usando SecurityContextHolderAwareRequestWrapper ".concat(auth.getName()).concat(" tienes acceso."));
		} else {
			logger.info("Hola usando SecurityContextHolderAwareRequestWrapper ".concat(auth.getName()).concat(" NO tienes acceso."));
		}
		
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Hola usando HttpServletRequest ".concat(auth.getName()).concat(" tienes acceso."));
		} else {
			logger.info("Hola usando HttpServletRequest ".concat(auth.getName()).concat(" NO tienes acceso."));
		}
		
		Pageable pageRequest = PageRequest.of(page, 5); // elementos por pagina.
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	/**
	 * Crear.
	 *
	 * @param model the model
	 * @param locale the locale
	 * @return the string
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model, Locale locale) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
		return "form";
	}
	
	/**
	 * Editar.
	 *
	 * @param id the id
	 * @param model the model
	 * @param flash the flash
	 * @param locale the locale
	 * @return the string
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		Cliente cliente = null;
		if(id > 0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
		return "form";
	}
	
	/**
	 * Guardar.
	 *
	 * @param cliente the cliente
	 * @param result the result
	 * @param model the model
	 * @param foto the foto
	 * @param flash the flash
	 * @param status the status
	 * @param locale the locale
	 * @return the string
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto,
				RedirectAttributes flash,SessionStatus status, Locale locale) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));
			return "form";
		}
		if(!foto.isEmpty()) {
			
			//borrar foto por si el usuario ya tuviese foto asi eliminamos archivos basura
			if(cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				uploadFileService.delete(cliente.getFoto());
			}
			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'" + uniqueFileName + "'");
			cliente.setFoto(uniqueFileName);
		}
		String mensajeFlash = (cliente.getId() != null) ? messageSource.getMessage("text.cliente.flash.editar.success", null, locale) : messageSource.getMessage("text.cliente.flash.crear.success", null, locale);
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	
	/**
	 * Eliminar.
	 *
	 * @param id the id
	 * @param flash the flash
	 * @param locale the locale
	 * @return the string
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {
		if(id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));
			//borrar la foto asociada al cliente
			if(uploadFileService.delete(cliente.getFoto())) {
				String mensajeFotoEliminar = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto());
				flash.addFlashAttribute("info", mensajeFotoEliminar);
			}
		}
		return "redirect:/listar";
	}
	
	/**
	 * Checks for role.
	 *
	 * @param role the role
	 * @return true, if successful
	 */
	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if(context == null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		if(auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority(role));
		/*for (GrantedAuthority authority : authorities) {
			if(role.equals(authority.getAuthority())) {
				logger.info("Hola usuario ".concat(auth.getName()).concat(" tu rol es: ").concat(authority.getAuthority()));
				return true;
			}
		}
		return false;*/
		
	}
}