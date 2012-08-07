package com.tau.ykesten.pinterest.index;

import java.util.Iterator;
import java.util.logging.Logger;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.openimaj.math.statistics.distribution.Histogram;

import com.tau.ykesten.pinterest.bin.HistogramDeserializer;
import com.tau.ykesten.pinterest.bin.Picture;

public abstract class Step {

	protected ObjectMapper mapper;
	protected String inputDirPath;
	protected String outputDirPath;
	protected final String stepName;
	protected final Logger logger;

	public Step(String inputDirPath, String outputDirPath, String stepName) {
		super();
		this.inputDirPath = inputDirPath;
		this.outputDirPath = outputDirPath;
		this.stepName = stepName;
		mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.getDeserializationConfig().addMixInAnnotations(Histogram.class,HistogramDeserializer.class);
		logger = Logger.getLogger(stepName);
	}

	public void runStep() {
		runStep(new Iterator<Picture>() {
			public boolean hasNext() {return false;}
			public Picture next() {return null;	}
			public void remove() {}
		});
	}
	
	public void runStep(Iterator<Picture> pics) {
		logger.info("Starting " + stepName);
		try {
			generate(pics);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warning("Error at step " + stepName + " : " + e.getMessage());
			return;
		}
		logger.info("Done " + stepName);
	}

	protected abstract void generate(Iterator<Picture> pics) throws Exception;

	public String getInputDirPath() {
		return inputDirPath;
	}

	public void setInputDirPath(String inputDirPath) {
		this.inputDirPath = inputDirPath;
	}

	public String getOutputDirPath() {
		return outputDirPath;
	}

	public void setOutputDirPath(String outputDirPath) {
		this.outputDirPath = outputDirPath;
	}

	public String getStepName() {
		return stepName;
	}

}
