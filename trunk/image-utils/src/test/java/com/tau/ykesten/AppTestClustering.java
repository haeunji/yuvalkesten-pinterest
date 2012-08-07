package com.tau.ykesten;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openimaj.math.statistics.distribution.Histogram;
import org.openimaj.ml.clustering.kmeans.fast.FastDoubleKMeansCluster;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.HistogramDeserializer;
import com.tau.ykesten.pinterest.bin.Picture;

public class AppTestClustering {

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
		// FileInputStream fis = new FileInputStream("data/LDAMODEL.bin");
		// ObjectInputStream ois = new ObjectInputStream(fis);
		// ParallelTopicModel model = (ParallelTopicModel) ois.readObject();
		// assert (model != null);
		//
		// final TopicInferencer inferencer = model.getInferencer();
		//
		// FileOutputStream fos = new FileOutputStream("data/INFERENCER.bin");
		// ObjectOutputStream oos = new ObjectOutputStream(fos);
		// oos.writeObject(inferencer);
		// oos.close();
		// fos.close();

		final TopicInferencer inferencer = TopicInferencer.read(new File("data/INFERENCER.bin"));

		final List<double[]> ldaVectors = new ArrayList<double[]>();
		final List<double[]> codebookVectors = new ArrayList<double[]>();
		final Map<String, double[]> pinIdToLdaVector = new HashMap<String, double[]>();

		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		pipeList.add(new PicJson2FeatureSequence(codebookVectors));

		pipeList.add(new Pipe() {
			private static final long serialVersionUID = 2101526980958023668L;

			@Override
			public Instance pipe(Instance inst) {
				try {

					if (inst == null || inst.getData() == null
							|| !(inst.getData().getClass().isAssignableFrom(FeatureSequence.class))) {
						return inst;
					}

					String pinID = inst.getName().toString();
					pinID = pinID.replaceAll(".*\\/pic\\-", "");
					pinID = pinID.replaceAll("\\.json", "");

					double[] testProbabilities = inferencer.getSampledDistribution(inst, 100, 10, 50);

					ldaVectors.add(testProbabilities);
					pinIdToLdaVector.put(pinID, testProbabilities);

					String name = inst.getName().toString();
					name = name.replaceAll(".*\\/pic\\-", "<a href=\"");
					name = name.replaceAll("\\.json", "/\">link</a><br>");
					// System.out.println(name + "\t" + argMax(testProbabilities));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return inst;
			}
		});

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));

		instances.addThruPipe(new FileIterator("data/pics-f/"));

		// Now the two lists are full - we can train the clustering object
		FastDoubleKMeansCluster cbCluster = new FastDoubleKMeansCluster(Commons.CODEBOOK_SIZE, 300, false);
		double[][] cbData = new double[codebookVectors.size()][Commons.CODEBOOK_SIZE];
		for (int i = 0; i < cbData.length; i++) {
			cbData[i] = codebookVectors.get(i);
		}
		cbCluster.train(cbData);
		cbCluster.optimize(false);

		FastDoubleKMeansCluster ldaCluster = new FastDoubleKMeansCluster(Commons.NUM_OF_LDA_TOPICS, 300, false);
		double[][] ldaData = new double[ldaVectors.size()][Commons.NUM_OF_LDA_TOPICS];
		for (int i = 0; i < ldaData.length; i++) {
			ldaData[i] = ldaVectors.get(i);
		}
		ldaCluster.train(ldaData);
		ldaCluster.optimize(false);

		System.out.println("link\tlda arg max\t cb arg max\tlda ass\tcb ass");
		for (File file : new File("data/pics-f/").listFiles()) {
			try {
				Picture pic = mapper.readValue(file, Picture.class);
				double[] codebookVector = pic.calcNormalizedCbVector();
				int cbAss = cbCluster.push_one(codebookVector);

				double[] ldaVector = pinIdToLdaVector.get(pic.getId());
				int ldaAss = ldaCluster.push_one(ldaVector);

				System.out.println("<img src=\"http://pinterest.com/pin/" + pic.getPin().getImages().getCloseup()
						+ "/\"/><br>" + "\t" + argMax(ldaVector) + "\t" + argMax(codebookVector) + "\t" + ldaAss + "\t"
						+ cbAss);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

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

		private final List<double[]> codebookVectors;

		public PicJson2FeatureSequence(final List<double[]> codebookVectors) {
			super(new Alphabet(alphabet), null);
			this.codebookVectors = codebookVectors;
		}

		public Instance pipe(Instance carrier) {
			try {
				File file = (File) carrier.getData();
				Picture pic;
				try {
					pic = mapper.readValue(file, Picture.class);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				double[] picCbVector = pic.calcNormalizedCbVector();
				if (picCbVector != null) {
					FeatureSequence featureSequence = new FeatureSequence(getAlphabet(), Commons.CODEBOOK_SIZE / 2);

					for (int i = 0; i < picCbVector.length; i++) {
						if (picCbVector[i] != 0) {
							featureSequence.add(i);
						}
					}

					codebookVectors.add(picCbVector);

					carrier.setData(featureSequence);
				} else {
					carrier.setData(null);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return carrier;
		}
	}

	public static void main(String[] args) {
		AppTestClustering atc = new AppTestClustering();
		atc.init();
		try {
			atc.testApp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
