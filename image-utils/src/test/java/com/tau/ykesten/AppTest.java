package com.tau.ykesten;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openimaj.math.statistics.distribution.Histogram;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.HistogramDeserializer;
import com.tau.ykesten.pinterest.bin.Picture;

public class AppTest {

	private static final Integer[] alphabet;
	protected ObjectMapper mapper;

	static {
		alphabet = new Integer[Commons.CODEBOOK_SIZE];
		for (int i = 0; i < Commons.CODEBOOK_SIZE; i++) {
			alphabet[i] = i;
		}
	}

	@Before
	public void init() {
		mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.getDeserializationConfig().addMixInAnnotations(Histogram.class, HistogramDeserializer.class);
	}

	@Test
	public void testApp() throws Exception {
		FileInputStream fis = new FileInputStream("data/50-LDAMODEL.bin");
		ObjectInputStream ois = new ObjectInputStream(fis);
		ParallelTopicModel model = (ParallelTopicModel) ois.readObject();
		assert (model != null);

		final TopicInferencer inferencer = model.getInferencer();

		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		pipeList.add(new PicJson2FeatureSequence());

		pipeList.add(new Pipe() {
			private static final long serialVersionUID = 2101526980958023668L;

			@Override
			public Instance pipe(Instance inst) {
				double[] testProbabilities = inferencer.getSampledDistribution(inst, 100, 10, 50);
				String name = inst.getName().toString();
				name = name.replaceAll(".*\\/pic\\-", "<a href=\"http://pinterest.com/pin/");
				name = name.replaceAll("\\.json", "/\">link</a><br>");
				System.out.println(name + "\t" + argMax(testProbabilities));
				return inst;
			}
		});

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));

		instances.addThruPipe(new FileIterator("data/pics-f-sample/"));

	}

	private static int argMax(double[] arr) {
		int argMax = -1;
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
				argMax = i;
			}
		}
		return argMax;
	}

	private class PicJson2FeatureSequence extends Pipe {

		private static final long serialVersionUID = 9139009386248442351L;

		public PicJson2FeatureSequence() {
			super(new Alphabet(alphabet), null);
		}

		public Instance pipe(Instance carrier) {
			File file = (File) carrier.getData();
			Picture pic;
			try {
				pic = mapper.readValue(file, Picture.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if (pic.getCodebookVector() != null) {
				FeatureSequence featureSequence = new FeatureSequence(getAlphabet(), Commons.CODEBOOK_SIZE / 2);

				int[] codebookVector = pic.getCodebookVector();
				for (int i = 0; i < codebookVector.length; i++) {
					if (codebookVector[i] != 0) {
						featureSequence.add(i);
					}
				}

				carrier.setData(featureSequence);
			} else {
				carrier.setData(null);
			}
			return carrier;
		}
	}
}
