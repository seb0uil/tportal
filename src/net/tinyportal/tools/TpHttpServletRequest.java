package net.tinyportal.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class TpHttpServletRequest extends HttpServletRequestWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1759777063952127274L;

	public TpHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Enumeration<String> getAttributeNames(){
		return new TpEnumeration<>(new ArrayList());
	}
	
	@Override
	public void removeAttribute(String name) {
		System.out.println("Remove Name :: " + name);
//		super.removeAttribute(name);
	}
	
	@Override
	public void setAttribute(String name, Object o) {
		System.out.println("Set Name :: " + name);
//		super.setAttribute(name, o);
	}
}

