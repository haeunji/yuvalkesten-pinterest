package com.tau.ykesten.pinterest.bin;

import java.util.HashSet;
import java.util.Set;

public class Picture {
	private Set<Pin> appearances;
	private String id;
	
	public Picture(){
		
	}
	
	public Picture(String id) {
		super();
		this.id = id;
		this.appearances = new HashSet<Pin>();
	}
	
	public Picture(String id, Set<Pin> appearances){
		this.id = id;
		this.appearances = appearances;
	}

	public void addAppearance(Pin pin){
		appearances.add(pin);
	}
	
	public Set<Pin> getAppearances() {
		return appearances;
	}

	public String getId() {
		return id;
	}
	
	public void setAppearances(Set<Pin> appearances) {
		this.appearances = appearances;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
