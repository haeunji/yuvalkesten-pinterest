package com.tau.ykesten.pinterest.bin;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.statistics.distribution.Histogram;

import com.tau.ykesten.pinterest.Commons;

public class Picture {

	private Pin pin;
	private byte[][] siftFeatures;
	private int[] codebookVector;
	private double[] normalizedCbVector;
	private int codebookCluster;
	private double[] ldaVector;
	private int ldaCluster;
	private Histogram histogram;

	public Picture() {
	}

	public Picture(Pin pin) {
		this.pin = pin;
	}

	public String getId() {
		return pin.getId();
	}

	public Pin getPin() {
		return pin;
	}

	public void setPin(Pin pin) {
		this.pin = pin;
	}

	public byte[][] getSiftFeatures() {
		return siftFeatures;
	}

	public void addSiftFeatures(LocalFeatureList<Keypoint> siftFeatures) {
		ArrayList<byte[]> tmp = new ArrayList<byte[]>(siftFeatures.size());
		for (Keypoint feature : siftFeatures) {
			tmp.add(feature.ivec);
		}
		this.siftFeatures = tmp.toArray(Commons.DUMMY_BYTE_ARRAY);
	}

	@JsonIgnore
	public void setSiftFeatures(byte[][] siftFeatures) {
		this.siftFeatures = siftFeatures;
	}

	public int[] getCodebookVector() {
		return codebookVector;
	}

	public void setCodebookVector(int[] codebookVector) {
		this.codebookVector = codebookVector;
		this.normalizedCbVector = Commons.normalize(codebookVector);
	}

	public double[] calcNormalizedCbVector() {
		return normalizedCbVector;
	}

	public int getCodebookCluster() {
		return codebookCluster;
	}

	public void setCodebookCluster(int codebookCluster) {
		this.codebookCluster = codebookCluster;
	}

	public double[] getLdaVector() {
		return ldaVector;
	}

	public void setLdaVector(double[] ldaVector) {
		this.ldaVector = ldaVector;
	}

	public int getLdaCluster() {
		return ldaCluster;
	}

	public void setLdaCluster(int ldaCluster) {
		this.ldaCluster = ldaCluster;
	}

	public Histogram getHistogram() {
		return histogram;
	}

	// @JsonDeserialize(using = HistogramDeserializer.class)
	@JsonIgnore
	public void setHistogram(Histogram histogram) {
		this.histogram = histogram;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<img src=\"");
		sb.append(getPin().getImages().getCloseup());
		sb.append("/\"/><br>");
		sb.append('\t');
		sb.append(Commons.argMax(ldaVector));
		sb.append('\t');
		sb.append(Commons.argMax(normalizedCbVector));
		sb.append('\t');
		sb.append(ldaCluster);
		sb.append('\t');
		sb.append(codebookCluster);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return pin.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Picture other = (Picture) obj;
		if (pin.getId() == null) {
			if (pin.getId() != null)
				return false;
		} else if (!pin.getId().equals(other.pin.getId()))
			return false;
		return true;
	}

}
