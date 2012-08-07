package com.tau.ykesten.pinterest.index;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.openimaj.math.statistics.distribution.Histogram;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.HistogramDeserializer;
import com.tau.ykesten.pinterest.bin.Picture;

public class IndexBuilder {

	private static final class RunnableImplementation implements Runnable {
		private final List<Picture> pics;
		private final ObjectMapper mapper;
		private final List<File> subList;
		private final AtomicInteger globalCounter;

		private RunnableImplementation(List<Picture> pics, ObjectMapper mapper, List<File> subList,
				AtomicInteger globalCounter) {
			this.pics = pics;
			this.mapper = mapper;
			this.subList = subList;
			this.globalCounter = globalCounter;
		}

		@Override
		public void run() {
			int localCounter = 0;
			for (File file : subList) {
				try {
					Picture pic = mapper.readValue(file, Picture.class);
					pics.add(pic);
					if (++localCounter % 500 == 0) {
						System.out.println("Parsed " + globalCounter.addAndGet(localCounter));
						localCounter = 0;
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {

		String workingDirPath = "data/";

		// Preliminary step:
		// PicsFetcher picsFetcher = new PicsFetcher("", workingDirPath);
		// picsFetcher.runStep();

		Step[] steps = {
				// new CodebookTrainer(workingDirPath, workingDirPath),
				// new CodebookAssigner(workingDirPath, workingDirPath),
				// new CodebookVectorClusterTrainer(workingDirPath, workingDirPath),
				// new CodebookVectorClusterAssigner(workingDirPath, workingDirPath),
				// new LdaTrainer(workingDirPath, workingDirPath),
				// new LdaAssigner(workingDirPath, workingDirPath),
				new LdaVectorClusterTrainer(workingDirPath, workingDirPath),
				new LdaVectorClusterAssigner(workingDirPath, workingDirPath),
				new PicsSerializer(workingDirPath, workingDirPath) };

		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.getDeserializationConfig().addMixInAnnotations(Histogram.class, HistogramDeserializer.class);

		int numOfThreads = Commons.NUM_OF_THREADS;
		ExecutorService pool = Executors.newFixedThreadPool(numOfThreads);

		File dir = new File("data/out-1");
		File[] arrFiles = dir.listFiles();
		List<File> files = Arrays.asList(arrFiles);
		Collections.shuffle(files);
		final List<Picture> pics = Collections.synchronizedList(new ArrayList<Picture>());
		int numOfFiles = files.size();
		int filesPerTask = numOfFiles / numOfThreads;

		AtomicInteger counter = new AtomicInteger();

		for (int i = 0; i < numOfFiles; i += filesPerTask) {
			final List<File> subList;
			if (i + filesPerTask <= numOfFiles) {
				subList = files.subList(i, i + filesPerTask);
			} else {
				subList = files.subList(i, numOfFiles);
			}
			pool.submit(new RunnableImplementation(pics, mapper, subList, counter));
		}

		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);

		for (Step step : steps) {
			step.runStep(pics.iterator());
		}

	}
}
