package com.tau.ykesten.pinterest.index;

import java.io.RandomAccessFile;
import java.util.Iterator;

import org.openimaj.ml.clustering.kmeans.fast.FastDoubleKMeansCluster;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Picture;

public class CodebookVectorClusterAssigner extends Step {

	public CodebookVectorClusterAssigner(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, CodebookVectorClusterAssigner.class.getSimpleName());
	}

	@Override
	protected void generate(Iterator<Picture> pics) throws Exception {
		FastDoubleKMeansCluster cbCluster = new FastDoubleKMeansCluster(Commons.CODEBOOK_SIZE,
				Commons.NUM_OF_CENTROIDS, false);
		cbCluster.readBinary(new RandomAccessFile(inputDirPath + Commons.CB_CLUSTER_BIN, "rw"));
		cbCluster.optimize(false);
		while (pics.hasNext()) {
			try {
				Picture pic = pics.next();
				double[] calcNormalizedCbVector = pic.calcNormalizedCbVector();
				if (calcNormalizedCbVector == null){
					continue;
				}
				int cbClusterAssignemnt = cbCluster.push_one(calcNormalizedCbVector);
				pic.setCodebookCluster(cbClusterAssignemnt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
