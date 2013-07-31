package net.tinyportal.tools;

import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;

public class TpClassLoader extends URLClassLoader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5711822518499208828L;

	public TpClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
}
