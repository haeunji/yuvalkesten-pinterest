package com.tau.ykesten.pinterest.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Test;

public class UserCrawlerTest {

	@Test
	public void testCrawlPages() throws IOException {
		execute();
	}

	private static void execute() throws IOException {
		Collection<String> baseUrls = new LinkedList<String>();
		baseUrls.add("http://pinterest.com/?");
		baseUrls.add("http://pinterest.com/popular/?");
		baseUrls.add("http://pinterest.com/all/?category=architecture&");
		baseUrls.add("http://pinterest.com/all/?category=art&");
		baseUrls.add("http://pinterest.com/all/?category=cars_motorcycles&");
		baseUrls.add("http://pinterest.com/all/?category=design&");
		baseUrls.add("http://pinterest.com/all/?category=diy_crafts&");
		baseUrls.add("http://pinterest.com/all/?category=education&");
		baseUrls.add("http://pinterest.com/all/?category=film_music_books&");
		baseUrls.add("http://pinterest.com/all/?category=fitness&");
		baseUrls.add("http://pinterest.com/all/?category=food_drink&");
		baseUrls.add("http://pinterest.com/all/?category=gardening&");
		baseUrls.add("http://pinterest.com/all/?category=geek&");
		baseUrls.add("http://pinterest.com/all/?category=hair_beauty&");
		baseUrls.add("http://pinterest.com/all/?category=history&");
		baseUrls.add("http://pinterest.com/all/?category=holidays&");
		baseUrls.add("http://pinterest.com/all/?category=home&");
		baseUrls.add("http://pinterest.com/all/?category=humor&");
		baseUrls.add("http://pinterest.com/all/?category=kids&");
		baseUrls.add("http://pinterest.com/all/?category=mylife&");
		baseUrls.add("http://pinterest.com/all/?category=women_apparel&");
		baseUrls.add("http://pinterest.com/all/?category=men_apparel&");
		baseUrls.add("http://pinterest.com/all/?category=outdoors&");
		baseUrls.add("http://pinterest.com/all/?category=people&");
		baseUrls.add("http://pinterest.com/all/?category=pets&");
		baseUrls.add("http://pinterest.com/all/?category=photography&");
		baseUrls.add("http://pinterest.com/all/?category=prints_posters&");
		baseUrls.add("http://pinterest.com/all/?category=products&");
		baseUrls.add("http://pinterest.com/all/?category=science&");
		baseUrls.add("http://pinterest.com/all/?category=sports&");
		baseUrls.add("http://pinterest.com/all/?category=technology&");
		baseUrls.add("http://pinterest.com/all/?category=travel_places&");
		baseUrls.add("http://pinterest.com/all/?category=wedding_events&");
		baseUrls.add("http://pinterest.com/all/?category=other&");

		Collection<String> urls = new LinkedList<String>();
		for (String baseUrl : baseUrls) {
			for (int i = 1; i < 11; i++) {
				urls.add(baseUrl + "marker=&page=" + i);
			}
		}

		Set<String> users = new HashSet<String>();
		int i = 1;
		while (true) {
			for (String url : urls) {
				try {
					System.out.println("@@@URL: " + url);
					Set<String> userIds = UserIdsFetcher.getUserIds(url);
					users.addAll(userIds);
				} catch (Exception e) {
					System.out.println("Error in url " + url + " Msg: " + e.getMessage());
				}
			}
			File f = new File("./data/ids/usersIds" + i + ".txt");
			BufferedWriter w = new BufferedWriter(new FileWriter(f,true));
			i++;
			for (String user : users) {
				System.out.println(user);
				w.write(user + "\n");
			}
			w.close();
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		execute();
	}

}
