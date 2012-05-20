package com.tau.ykesten.pinterest.aggregate;

import java.util.HashMap;
import java.util.Map;

import com.tau.ykesten.pinterest.bin.Picture;
import com.tau.ykesten.pinterest.bin.Pin;

public class PinsAggregator {

	private final Map<String, Picture> urlToPicture;

	public PinsAggregator() {
		urlToPicture = new HashMap<String, Picture>();
	}

	public void process(Pin pin) {
		// if (urlToPicture.containsKey(pin.))
	}

}
