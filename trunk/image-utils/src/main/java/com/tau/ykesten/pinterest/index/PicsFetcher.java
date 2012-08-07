package com.tau.ykesten.pinterest.index;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;

import com.tau.ykesten.pinterest.Commons;
import com.tau.ykesten.pinterest.bin.Board;
import com.tau.ykesten.pinterest.bin.Picture;
import com.tau.ykesten.pinterest.bin.Pin;
import com.tau.ykesten.pinterest.bin.User;

public class PicsFetcher extends Step {
	private final class RunnableImplementation implements Runnable {
		private final Pin pin;

		private RunnableImplementation(Pin pin) {
			this.pin = pin;
		}

		@Override
		public void run() {
			try {
				processPin(pin);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private final AtomicInteger counter = new AtomicInteger();
	private final BlockingQueue<DoGSIFTEngine> engines;

	public PicsFetcher(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, PicsFetcher.class.getSimpleName());
		engines = new ArrayBlockingQueue<DoGSIFTEngine>(Commons.NUM_OF_THREADS);
		for (int i = 0; i < Commons.NUM_OF_THREADS; i++) {
			engines.add(new DoGSIFTEngine());
		}
	}

	@Override
	protected void generate(Iterator<Picture> pics) throws Exception {
		String dirPath = inputDirPath;
		File dir = new File(dirPath);
		File[] listFiles = dir.listFiles();
		List<File> files = Arrays.asList(listFiles);
		Collections.shuffle(files);
		listFiles = files.toArray(listFiles);

		ExecutorService pool = Executors.newFixedThreadPool(Commons.NUM_OF_THREADS);

		for (File file : listFiles) {
			try {
				User user = mapper.readValue(file, User.class);
				List<Board> boards = user.getBoards();
				for (Board board : boards) {
					for (final Pin pin : board.getPins()) {
						pool.submit(new RunnableImplementation(pin));
						int tmpCounter = counter.get();
						if (tmpCounter % 1000 == 0) {
							System.out.println(counter + " out of " + listFiles.length);
						}
						if (tmpCounter >= Commons.DB_SIZE) {
							pool.shutdown();
							pool.awaitTermination(2, TimeUnit.MINUTES);
							return;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		pool.shutdown();
	}

	private void processPin(Pin pin) throws Exception {
		File imageFile = new File(outputDirPath+"/images/image-" + pin.getId() + ".gif");
		if (imageFile.exists()) {
			return;
		}

		MBFImage image = ImageUtilities.readMBF(new URL(pin.getImages().getCloseup()));
		ImageUtilities.write(image, imageFile);
		DoGSIFTEngine engine = engines.poll();
		LocalFeatureList<Keypoint> keypoints = engine.findFeatures(image.flatten());
		engines.offer(engine);
		Picture pic = new Picture(pin);
		pic.addSiftFeatures(keypoints);
		mapper.writeValue(new File(outputDirPath+"/pics/pic-" + pin.getId() + ".json"), pic);
		counter.incrementAndGet();
	}

}
