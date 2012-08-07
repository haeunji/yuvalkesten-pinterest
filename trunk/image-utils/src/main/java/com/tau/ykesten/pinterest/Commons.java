package com.tau.ykesten.pinterest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import cc.mallet.types.Alphabet;

public class Commons {
	public static final int CODEBOOK_SIZE = 2000;
	public static final int NUM_OF_HISTOGRAM_BINS = 256;
	public static final boolean DO_HISTOGRAM = true;
	public static final int NUM_OF_THREADS = 4;
	public static final int TRAIN_SET_SIZE = 5000;
	public static final int DB_SIZE = 1000000;
	public static final byte[][] DUMMY_BYTE_ARRAY = new byte[128][];
	public static final int[] DUMMY_INT_ARRAY = new int[1];
	public static final Integer[] DUMMY_INTEGER_ARRAY = new Integer[1];
	public static final int NUM_OF_LDA_TOPICS = 200;
	public static final int NUM_OF_PENDING_TASKS = 50;
	public static final String CB_CLUSTER_BIN = "/cbCluster.bin";
	public static final String CB_BIN = "/codebook.bin";
	public static final int NUM_OF_CENTROIDS = 300;
	public static final String INFERENCER_BIN = "/INFERENCER.bin";
	public static final Alphabet ALPHABET;
	public static final String LDA_CLUSTER_BIN = "/ldaCluster.bin";

	static {
		Integer[] tmp = new Integer[CODEBOOK_SIZE];
		for (int i = 0; i < CODEBOOK_SIZE; i++) {
			tmp[i] = i;
		}
		ALPHABET = new Alphabet(tmp);
	}

	public static int[] toIntArray(Integer[] source) {
		int[] res = new int[source.length];
		int i = 0;
		for (Integer integer : source) {
			res[i++] = integer;
		}
		return res;
	}

	public static Integer[] toIntegerArray(int[] source) {
		Integer[] res = new Integer[source.length];
		int i = 0;
		for (int integer : source) {
			res[i++] = integer;
		}
		return res;
	}

	public static int[] toIntArray(List<Integer> source) {
		int[] res = new int[source.size()];
		int i = 0;
		for (Integer integer : source) {
			res[i++] = integer;
		}
		return res;
	}

	public static double[] normalize(int[] codebookVector) {
		int norm = 0;
		for (int integer : codebookVector) {
			norm += integer;
		}
		if (norm == 0) {
			return null;
		}
		double[] result = new double[codebookVector.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (double) codebookVector[i] / (double) norm;
		}

		return result;
	}

	public static int argMax(double[] arr) {
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

	public static void writeObject(Object o, String path) throws Exception {
		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(o);
		oos.close();
		fos.close();
	}

	public static Object readObject(String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object result = ois.readObject();
		ois.close();
		fis.close();
		return result;
	}
}
