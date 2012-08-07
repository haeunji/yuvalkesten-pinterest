package com.tau.ykesten.pinterest.index;

import java.io.File;
import java.util.Iterator;

import com.tau.ykesten.pinterest.bin.Picture;

public class PicsSerializer extends Step {

	public PicsSerializer(String inputDirPath, String outputDirPath) {
		super(inputDirPath, outputDirPath, PicsSerializer.class.getSimpleName());
	}

	@Override
	protected void generate(Iterator<Picture> pics) throws Exception {
		System.out.println("link\tlda arg max\t cb arg max\tlda ass\tcb ass");
		while (pics.hasNext()) {
			try {
				Picture pic = pics.next();
				mapper.writeValue(new File(outputDirPath + "/out/pic-" + pic.getId() + ".json"), pic);
				System.out.println(pic);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
