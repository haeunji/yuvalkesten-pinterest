package com.tau.ykesten.pinterest;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.tau.ykesten.pinterest.bin.Board;
import com.tau.ykesten.pinterest.bin.Pin;
import com.tau.ykesten.pinterest.bin.User;
import com.tau.ykesten.pinterest.crawler.UserFullFetcher;

public class JsonParseTest extends TestCase {

	@Test
	public void testUser() {
		User user = UserFullFetcher.getUser("stephydasilva");
		assertNotNull(user);
		List<Board> boards = user.getBoards();
		assertTrue(boards != null && !boards.isEmpty());
		user.setBoards(boards);
		for (Board board : boards) {
			List<Pin> pins = board.getPins();
			assertTrue(pins != null && !pins.isEmpty());
			for (Pin pin : pins) {
				System.out.println(pin.getImages().getMobile());
			}
		}
	}

}
