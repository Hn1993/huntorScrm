package com.huntor.mscrm.app.model;

import java.io.Serializable;

public class Login implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String secret;
	@Override
	public String toString() {
		return "Login [id=" + id + ", secret=" + secret + "]";
	}
	
}
