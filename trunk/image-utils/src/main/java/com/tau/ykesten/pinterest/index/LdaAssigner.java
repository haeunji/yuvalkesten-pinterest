package com.tau.ykesten.pinterest.index;

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Picture;

public class LdaAssigner extends Step {

	private final class PicTask implements Runnable {
		private final Picture pic;

		private PicTask(Picture pic) {
			this.pic = pic;
		}

		@Override
		public void run() {
			try {
				double[] picCbVector = pic.calcNormalizedCbVector();
				if (picCbVector != null) {
					FeatureSequence featureSequence = new FeatureSequence(Commons.ALPHABET, Commons.CODEBOOK_SIZE / 2);

					for (int i = 0; i < picCbVector.length; i++) {
						if (picCbVector[i] != 0) {
							featureSequence.add(i);
						}
					}
					TopicInferencer inferencer = inferencers.poll();
					double[] ldaVector = inferencer.getSampledDistribution(new Instance(featureSequence, null, null,
							null), 100, 10, 50);
					inferencers.offer(inferencer);
					pic.setLdaVector(ldaVector);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	BlockingQueue<TopicInferencer> inferencers;

	public LdaAssigner(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, LdaAssigner.class.getSimpleName());
		inferencers = new LinkedBlockingQueue<TopicInferencer>(Commons.NUM_OF_THREADS);
		for (int i = 0; i < Commons.NUM_OF_THREADS; i++) {
			try {
				final TopicInferencer inferencer = TopicInferencer
						.read(new File(inputDirPath + Commons.INFERENCER_BIN));
				inferencers.add(inferencer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void generate(Iterator<Picture> pics) throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(Commons.NUM_OF_THREADS);
		while (pics.hasNext()) {
			final Picture pic = pics.next();
			pool.submit(new PicTask(pic));
		}
		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);
	}

}
