package com.ideas.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * The Interface IUploadFileService. Se encarga del manejo de ficheros.
 * @author Israel Bejarano
 */
public interface IUploadFileService {
	
	/**
	 * Load.
	 *
	 * @param filename the filename
	 * @return the resource
	 */
	public Resource load(String filename) throws MalformedURLException;
	
	/**
	 * Copy.
	 *
	 * @param file the file
	 * @return the string
	 */
	public String copy(MultipartFile file) throws IOException;
	
	/**
	 * Delete.
	 *
	 * @param filename the filename
	 * @return true, if successful
	 */
	public boolean delete(String filename);
	
	/**
	 * Delete all.
	 */
	public void deleteAll();
	
	/**
	 * Inits the.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void init() throws IOException;
}