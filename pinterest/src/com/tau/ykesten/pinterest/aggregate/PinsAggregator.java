package com.tau.ykesten.pinterest.aggregate;

import java.util.HashMap;
import java.util.Map;

import com.tau.ykesten.pinterest.bin.Picture;
import com.tau.ykesten.pinterest.bin.Pin;

public class PinsAggregator {

	private final Map<String, Picture> idToPicture;

	public PinsAggregator() {
		idToPicture = new HashMap<String, Picture>();
	}  

	public void process(Pin pin) {
		Picture picture = idToPicture.get(pin.getId());
		if (picture == null){
			picture = new Picture(pin.getId());
			idToPicture.put(pin.getId(), picture);
		}
		picture.addAppearance(pin);
	}

	public Map<String, Picture> getIdToPicture() {
		return idToPicture;
	}

}
