package com.tau.ykesten.pinterest.index;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Picture;

public class LdaTrainer extends Step {

	private static final Integer[] alphabet;

	static {
		alphabet = new Integer[Commons.CODEBOOK_SIZE];
		for (int i = 0; i < Commons.CODEBOOK_SIZE; i++) {
			alphabet[i] = i;
		}
	}

	private class PicJson2FeatureSequence extends Pipe {

		private static final long serialVersionUID = 9139009386248442351L;
		private int counter = 0;

		public PicJson2FeatureSequence() {
			super(new Alphabet(alphabet), null);
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
				if (pic.getCodebookVector() != null) {
					FeatureSequence featureSequence = new FeatureSequence(getAlphabet(), Commons.CODEBOOK_SIZE / 2);

					int[] codebookVector = pic.getCodebookVector();
					for (int i = 0; i < codebookVector.length; i++) {
						if (codebookVector[i] != 0) {
							featureSequence.add(i);
						}
					}

					carrier.setData(featureSequence);
					if (++counter % 500 == 0) {
						logger.info("Extracting codebook before topic modeling - " + counter);
					}
				} else {
					carrier.setData(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return carrier;
		}
	}

	public LdaTrainer(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, LdaTrainer.class.getSimpleName());
	}

	@Override
	protected void generate(Iterator<Picture> pics) throws Exception {
		// Begin by importing documents from text to feature sequences
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		pipeList.add(new PicJson2FeatureSequence());

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));

		instances.addThruPipe(new FileIterator(inputDirPath));

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		ParallelTopicModel model = new ParallelTopicModel(Commons.NUM_OF_LDA_TOPICS, 0.01, 0.01);

		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and combine
		// statistics after every iteration.
		model.setNumThreads(2);

		model.setNumIterations(1000);
		model.estimate();

		// Serialize the model inferencer
		try {
			FileOutputStream fileOut = new FileOutputStream(outputDirPath + Commons.INFERENCER_BIN);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(model.getInferencer());
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
