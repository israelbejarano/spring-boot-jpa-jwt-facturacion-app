package com.ideas.springboot.app.util.paginator;

/**
 * The Class PageItem.
 * @author Israel Bejarano
 */
public class PageItem {
	
	/** The numero. */
	private int numero;
	
	/** The actual. */
	private boolean actual;
	
	/**
	 * Instantiates a new page item.
	 *
	 * @param numero the numero
	 * @param actual the actual
	 */
	public PageItem(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}
	
	/**
	 * Gets the numero.
	 *
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}
	
	/**
	 * Checks if is actual.
	 *
	 * @return true, if is actual
	 */
	public boolean isActual() {
		return actual;
	}
}