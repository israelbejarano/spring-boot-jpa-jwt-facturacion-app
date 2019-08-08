package com.ideas.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * The Class UploadFilseServiceImpl. Implemente los métodos para el 
 * manejo de archivos en la aplicación
 * @author Israel Bejarano
 */
@Service
public class UploadFilseServiceImpl implements IUploadFileService {
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/** The Constant UPLOADS_FOLDER. */
	private final static String UPLOADS_FOLDER = "uploads";

	/**
	 * Load.
	 *
	 * @param filename the filename
	 * @return the resource
	 * @throws MalformedURLException the malformed URL exception
	 */
	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + pathFoto);
		Resource recurso = null;
		recurso = new UrlResource(pathFoto.toUri());
		if(!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		}
		return recurso;
	}

	/**
	 * Copy.
	 *
	 * @param file the file
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public String copy(MultipartFile file) throws IOException{
		String uniqueFileName =  UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueFileName);
		log.info("rootPath: " + rootPath);	// path relativo al proyecto
		Files.copy(file.getInputStream(), rootPath);
		return uniqueFileName;
	}

	/**
	 * Delete.
	 *
	 * @param filename the filename
	 * @return true, if successful
	 */
	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();
		if(archivo.exists() && archivo.canRead()) {
			if(archivo.delete()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the path.
	 *
	 * @param filename the filename
	 * @return the path
	 */
	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectories(Paths.get(UPLOADS_FOLDER));	
	}
}