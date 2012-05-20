package com.tau.ykesten.pinterest.bin;

import java.util.HashSet;
import java.util.Set;

public class Picture {
	private final Set<Pin> appearances;
	private final String url;
	
	public Picture(String url) {
		super();
		this.url = url;
		this.appearances = new HashSet<Pin>();
	}

	public void addAppearance(Pin pin){
		appearances.add(pin);
	}
	
	@Override
	public int hashCode() {
		return url.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Picture other = (Picture) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
}
