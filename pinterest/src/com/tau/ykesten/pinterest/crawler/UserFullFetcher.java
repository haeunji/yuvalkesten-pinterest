package com.tau.ykesten.pinterest.crawler;

import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.tau.ykesten.pinterest.bin.Board;
import com.tau.ykesten.pinterest.bin.BoardResponse;
import com.tau.ykesten.pinterest.bin.Pin;
import com.tau.ykesten.pinterest.bin.User;
import com.tau.ykesten.pinterest.bin.UserResponse;

public class UserFullFetcher {
	private static final String BOARD_URL = "https://api.pinterest.com/v2/boards";
	private static final String USER_URL = "https://api.pinterest.com/v2/users/";

	public static User getUser(String userId) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			String rawResponse = UrlFetcher.getRawResponse(USER_URL + userId);
			if (rawResponse == null){
				return null;
			}
			ObjectReader reader = mapper.reader(UserResponse.class);
			UserResponse userResponse = reader.readValue(rawResponse);
			User user = userResponse.getUser();
			List<Board> boards = userResponse.getBoards();
			user.setBoards(boards);
			for (Board board : boards) {
				try {
					rawResponse = UrlFetcher.getRawResponse(BOARD_URL + board.getUrl());
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				reader = mapper.reader(BoardResponse.class);
				BoardResponse boardResponse = reader.readValue(rawResponse);
				List<Pin> pins = boardResponse.getPins();
				board.setPins(pins);
			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
