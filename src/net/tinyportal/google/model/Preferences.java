/*
    This file is part of tPortal.

    tPortal is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    tPortal is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with tPortal.  If not, see <http://www.gnu.org/licenses/>.

    The original code was written by Sebastien Bettinger <sebastien.bettinger@gmail.com>

 */

/**
 * La classe Preferences représente le model utilisé pour stocker les préférences
 * des portlets au sein du DAO
 */
package net.tinyportal.google.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Serialized;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Preferences {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

	@Persistent
	private String portletName;
	
	@Persistent
	@Serialized
	private Map<String, List<String>> portletPreference;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getPortletName() {
		return portletName;
	}

	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	public Map<String, String[]> getPortletPreference() {
		Map<String, String[]> m = new HashMap<String, String[]>();
		
		for (String key : portletPreference.keySet()) {
			List<String> l = portletPreference.get(key);
			String[] s = l.toArray(new String[l.size()]);
			m.put(key, s);
		}
		return m;
	}

	public void setPortletPreference(Map<String, String[]> portletPreference) {
		this.portletPreference = new HashMap<String, List<String>>();
		for (String key : portletPreference.keySet()) {
			String[] s = portletPreference.get(key);
			List<String> l = new ArrayList(Arrays.asList(s));
			this.portletPreference.put(key, l);
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result
				+ ((portletName == null) ? 0 : portletName.hashCode());
		result = prime
				* result
				+ ((portletPreference == null) ? 0 : portletPreference
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Preferences other = (Preferences) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (portletName == null) {
			if (other.portletName != null)
				return false;
		} else if (!portletName.equals(other.portletName))
			return false;
		if (portletPreference == null) {
			if (other.portletPreference != null)
				return false;
		} else if (!portletPreference.equals(other.portletPreference))
			return false;
		return true;
	}

	
}
