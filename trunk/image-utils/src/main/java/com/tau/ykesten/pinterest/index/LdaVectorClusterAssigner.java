package com.tau.ykesten.pinterest.index;

import java.io.RandomAccessFile;
import java.util.Iterator;

import org.openimaj.ml.clustering.kmeans.fast.FastDoubleKMeansCluster;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Picture;

public class LdaVectorClusterAssigner extends Step {

	public LdaVectorClusterAssigner(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, LdaVectorClusterAssigner.class.getSimpleName());
	}

	@Override
	protected void generate(Iterator<Picture> pics) throws Exception {
		FastDoubleKMeansCluster ldaCluster = new FastDoubleKMeansCluster(Commons.NUM_OF_LDA_TOPICS,
				Commons.NUM_OF_CENTROIDS, false);
		ldaCluster.readBinary(new RandomAccessFile(inputDirPath + Commons.LDA_CLUSTER_BIN, "rw"));
		ldaCluster.optimize(false);
		while (pics.hasNext()) {
			try {
				Picture pic = pics.next();
				int ldaClusterAssignment = ldaCluster.push_one(pic.getLdaVector());
				pic.setLdaCluster(ldaClusterAssignment);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
