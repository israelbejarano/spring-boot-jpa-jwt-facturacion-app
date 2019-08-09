package com.ideas.springboot.app.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class SimpleGrantedAuthoritiesMixin. Clase para poder parsear la petición a json
 * @author Israel Bejarano
 */
public abstract class SimpleGrantedAuthorityMixin {
	
	/**
	 * Instantiates a new simple granted authorities mixin.
	 *
	 * @param role the role
	 */
	@JsonCreator
	public SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {
		
	}

}
