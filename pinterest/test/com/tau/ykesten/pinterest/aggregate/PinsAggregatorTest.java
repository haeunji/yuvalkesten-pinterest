package com.tau.ykesten.pinterest.aggregate;

import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tau.ykesten.pinterest.bin.Board;
import com.tau.ykesten.pinterest.bin.Picture;
import com.tau.ykesten.pinterest.bin.Pin;
import com.tau.ykesten.pinterest.bin.User;

public class PinsAggregatorTest {

	private static final String AGGREGATED_DATA_PATH = "./data/out/users.json";
	private ObjectMapper mapper;
	private ObjectReader userReader;
	private ObjectReader aggregatorReader;
	private ObjectWriter writer;
	
	@Before
	public void init(){
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		userReader = mapper.reader(User.class);
		aggregatorReader = mapper.reader(PinsAggregator.class);
		writer = mapper.writer();
	}

	// @Test
	public void testCreation() {
		PinsAggregator aggregator = new PinsAggregator();
		String dirPath = "./data/users/";
		File dir = new File(dirPath);
		File[] listFiles = dir.listFiles();
		int counter = 0;
		for (File file : listFiles) {
			try {
				if (++counter % 100 == 0) {
					System.out.println(counter + " out of " + listFiles.length);
				}
				User user = userReader.readValue(file);
				assertNotNull(user);
				List<Board> boards = user.getBoards();
				for (Board board : boards) {
					for (Pin pin : board.getPins()) {
						aggregator.process(pin);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File output = new File(AGGREGATED_DATA_PATH);
		try {
			output.createNewFile();
			writer.writeValue(output, aggregator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAggregatedData() throws Exception {
		File input = new File(AGGREGATED_DATA_PATH);
		PinsAggregator aggregator = aggregatorReader.readValue(input);
		Map<String, Picture> idToPicture = aggregator.getIdToPicture();
		for (Entry<String, Picture> entry : idToPicture.entrySet()) {
			Picture pic = entry.getValue();
			if (pic.getAppearances().size() > 1){
				System.out.println(pic.getId()+" " + pic.getAppearances().iterator().next().getImages().getThumbnail());
			}
		}
	}

}
