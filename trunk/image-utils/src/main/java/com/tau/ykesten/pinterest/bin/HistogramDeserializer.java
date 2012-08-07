package com.tau.ykesten.pinterest.bin;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.openimaj.math.statistics.distribution.Histogram;

import com.tau.ykesten.pinterest.Commons;

public class HistogramDeserializer extends JsonDeserializer<Histogram> {

	@Override
	public Histogram deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		return new Histogram(Commons.NUM_OF_HISTOGRAM_BINS);
	}
}
