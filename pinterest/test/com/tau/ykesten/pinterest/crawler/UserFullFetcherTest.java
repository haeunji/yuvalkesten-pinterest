package com.tau.ykesten.pinterest.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tau.ykesten.pinterest.bin.User;

public class UserFullFetcherTest {

	public static void fillUsers() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		File f = new File("C:\\pinterest\\usersIds66.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while ((line = br.readLine()) != null) {
			try {
				File userJsonFile = new File("C:\\pinterest\\newusers2\\" + line + ".json");
				System.out.print("Fetching "+line+"...");
				User user = UserFullFetcher.getUser(line);
				objectMapper.writeValue(userJsonFile, user);
				System.out.println("DONE");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		br.close();
	}

	public static void main(String[] args) throws IOException {
		fillUsers();
	}
}
