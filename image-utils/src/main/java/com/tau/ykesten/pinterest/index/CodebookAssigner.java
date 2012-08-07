package com.tau.ykesten.pinterest.index;

import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openimaj.ml.clustering.kmeans.fast.FastByteKMeansCluster;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Picture;

public class CodebookAssigner extends Step {

	private final class RunnableImplementation implements Runnable {
		private final FastByteKMeansCluster cluster;
		private final Picture pic;

		private RunnableImplementation(Picture pic, FastByteKMeansCluster cluster) {
			this.cluster = cluster;
			this.pic = pic;
		}

		@Override
		public void run() {
			try {
				processPic(pic, cluster);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public CodebookAssigner(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, CodebookAssigner.class.getSimpleName());
	}

	public void generate(Iterator<Picture> pics) throws Exception {

		final FastByteKMeansCluster cluster = new FastByteKMeansCluster(128, Commons.CODEBOOK_SIZE, false);
		cluster.readBinary(new RandomAccessFile(outputDirPath + Commons.CB_BIN, "rw"));
		cluster.optimize(false);

		ExecutorService pool = Executors.newFixedThreadPool(Commons.NUM_OF_THREADS);

		while (pics.hasNext()) {
			pool.submit(new RunnableImplementation(pics.next(), cluster));
		}

		pool.awaitTermination(1, TimeUnit.DAYS);
	}

	private void processPic(Picture pic, FastByteKMeansCluster cluster) throws Exception {
		int[] codebook = new int[Commons.CODEBOOK_SIZE];
		pic.setCodebookVector(codebook);
		for (byte[] feature : pic.getSiftFeatures()) {
			try {
				if (feature == null) {
					continue;
				}
				int centroid = cluster.push_one(feature);
				codebook[centroid]++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}