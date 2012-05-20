package com.tau.ykesten.pinterest.crawler;

import java.util.Set;

import org.junit.Test;

public class UserIdsFetcherTest {
	@Test
	public void testUserIdsFetcher() {
		Set<String> list = UserIdsFetcher.getUserIds("http://pinterest.com/popular/");
		for (String string : list) {
			System.out.println(string);
		}
//		String input = "                            <a href=\"/marybacon/\" class=\"ImgLink\">";
//		Pattern userIdPattern = Pattern.compile("\\s*\\<a\\shref=\"/([A-Za-z0-9]+)/\"\\sclass=\"ImgLink\"\\>");
//		Matcher matcher = userIdPattern.matcher(input);
//		assertTrue(matcher.matches());
//		System.out.println(matcher.group(1));
	}
}
