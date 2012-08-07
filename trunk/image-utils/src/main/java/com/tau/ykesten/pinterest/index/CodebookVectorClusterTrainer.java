package com.tau.ykesten.pinterest.index;

import java.io.DataOutput;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openimaj.ml.clustering.kmeans.fast.FastDoubleKMeansCluster;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Picture;

public class CodebookVectorClusterTrainer extends Step {

	private final class PicRunnable implements Runnable {

		private Object workingPermit;
		private Picture pic;

		public PicRunnable(Object workingPermit, Picture pic) {
			this.workingPermit = workingPermit;
			this.pic = pic;
		}

		@Override
		public void run() {
			double[] calcNormalizedCbVector = pic.calcNormalizedCbVector();
			if (calcNormalizedCbVector != null)
				codebookVectors.add(calcNormalizedCbVector);
			workingPermits.offer(workingPermit);
		}
	}

	private final ArrayList<double[]> notSyncornizedCbVector;
	private final List<double[]> codebookVectors;
	private final BlockingQueue<Object> workingPermits;

	public CodebookVectorClusterTrainer(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, CodebookVectorClusterTrainer.class.getSimpleName());
		notSyncornizedCbVector = new ArrayList<double[]>();
		codebookVectors = Collections.synchronizedList(notSyncornizedCbVector);
		workingPermits = new ArrayBlockingQueue<Object>(Commons.NUM_OF_PENDING_TASKS);
		for (int i = 0; i < Commons.NUM_OF_PENDING_TASKS; i++) {
			workingPermits.add(new Object());
		}
	}

	@Override
	protected void generate(Iterator<Picture> pics) throws Exception {

		ExecutorService pool = Executors.newFixedThreadPool(Commons.NUM_OF_THREADS);

		while (pics.hasNext()) {
			try {
				Object workingPermit = workingPermits.poll();
				Picture pic = pics.next();
				pool.submit(new PicRunnable(workingPermit, pic));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);

		FastDoubleKMeansCluster cbCluster = new FastDoubleKMeansCluster(Commons.CODEBOOK_SIZE,
				Commons.NUM_OF_CENTROIDS, false);
		double[][] cbData = new double[codebookVectors.size()][Commons.CODEBOOK_SIZE];
		for (int i = 0; i < cbData.length;) {
			double[] ds = codebookVectors.get(i);
			if (ds != null) {
				cbData[i] = ds;
				i++;
			}
		}
		cbCluster.train(cbData);
		DataOutput out = new RandomAccessFile(outputDirPath + Commons.CB_CLUSTER_BIN, "rw");
		cbCluster.writeBinary(out);
	}

}
