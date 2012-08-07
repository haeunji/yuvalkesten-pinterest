package com.tau.ykesten.pinterest.index;

import java.io.DataOutput;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

import org.openimaj.ml.clustering.kmeans.fast.FastByteKMeansCluster;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Picture;

public class CodebookTrainer extends Step {

	public CodebookTrainer(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, CodebookTrainer.class.getSimpleName());
	}

	public void generate(Iterator<Picture> pics) throws Exception {
		FastByteKMeansCluster cluster = new FastByteKMeansCluster(128, Commons.CODEBOOK_SIZE, false);
		ArrayList<byte[]> allFeatures = new ArrayList<byte[]>();

		int count = 0;
		while(pics.hasNext()) {
			try {
				if (count++ == Commons.TRAIN_SET_SIZE) {
					break;
				}
				Picture pic = pics.next();
				for (byte[] feature : pic.getSiftFeatures()) {
					if (feature != null) {
						allFeatures.add(feature);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.info("Created SIFT matrix. Training cluster");

		int res = cluster.train(allFeatures.toArray(Commons.DUMMY_BYTE_ARRAY));
		if (res == 1) {
			throw new Exception("Cluster train failed");
		}
		DataOutput out = new RandomAccessFile(outputDirPath + Commons.CB_BIN, "rw");
		cluster.writeBinary(out);
	}
}
