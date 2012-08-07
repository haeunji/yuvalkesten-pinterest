package com.tau.ykesten.pinterest.index;

import java.io.DataOutput;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openimaj.ml.clustering.kmeans.fast.FastDoubleKMeansCluster;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Picture;

public class LdaVectorClusterTrainer extends Step {

	public LdaVectorClusterTrainer(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, LdaVectorClusterTrainer.class.getSimpleName());
	}

	@Override
	protected void generate(Iterator<Picture> pics) throws Exception {

		List<double[]> ldaVectors = new ArrayList<double[]>();

		while (pics.hasNext()) {
			try {
				Picture pic = pics.next();
				double[] ldaVector = pic.getLdaVector();
				if (ldaVector != null) {
					ldaVectors.add(ldaVector);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		FastDoubleKMeansCluster ldaCluster = new FastDoubleKMeansCluster(Commons.NUM_OF_LDA_TOPICS,
				Commons.NUM_OF_CENTROIDS, false);
		double[][] ldaData = new double[ldaVectors.size()][Commons.NUM_OF_LDA_TOPICS];
		for (int i = 0; i < ldaData.length; i++) {
			ldaData[i] = ldaVectors.get(i);
		}
		ldaCluster.train(ldaData);
		DataOutput out = new RandomAccessFile(outputDirPath + Commons.LDA_CLUSTER_BIN, "rw");
		ldaCluster.writeBinary(out);
		System.out.println(ldaCluster.toString());
	}

}
