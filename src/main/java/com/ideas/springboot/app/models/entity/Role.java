package com.ideas.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Class Role.
 * @author Israel Bejarano
 */
@Entity
@Table(name="authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "authority"})})
public class Role implements Serializable {
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The authority. */
	private String authority;
	
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
	 * Gets the authority.
	 *
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * Sets the authority.
	 *
	 * @param authority the new authority
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
}