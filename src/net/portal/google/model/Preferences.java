package net.portal.google.model;

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
