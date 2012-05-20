package com.tau.ykesten.pinterest.crawler;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tau.ykesten.pinterest.bin.User;

public class UserCrawler {

	private static final int NUM_OF_THREADS = 1;// We have rate limiting so no sense in
	private final ObjectWriter writer;

	public UserCrawler() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
		writer = mapper.writerWithType(User.class);
	}

	public Set<User> crawlPage(String url) {
		System.out.println("Crawling page " + url);
		Set<User> result = new HashSet<User>();
		Set<String> userIds = UserIdsFetcher.getUserIds(url);
		if (userIds == null) {
			return result;
		}
		for (String userId : userIds) {
			User user = UserFullFetcher.getUser(userId);
			if (user != null) {
				result.add(user);
				try {
					writer.writeValue(new File("C:\\pinterest\\users\\" + user.getId() + ".json"), user);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			else{
				System.err.println("User is NULL: "+userId);
			}
		}
		System.out.println("DONE Crawling page " + url);
		return result;
	}

	public Set<User> crawlPages(Collection<String> urls) {
		Set<User> result = new HashSet<User>();
		ExecutorService pool = Executors.newFixedThreadPool(NUM_OF_THREADS);
		Collection<CrawlTask> tasks = new LinkedList<CrawlTask>();
		for (String url : urls) {
			tasks.add(new CrawlTask(url));
		}
		try {
			List<Future<Set<User>>> invokes = pool.invokeAll(tasks);
			for (Future<Set<User>> future : invokes) {
				result.addAll(future.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private class CrawlTask implements Callable<Set<User>> {

		private String url;

		public CrawlTask(String url) {
			super();
			this.url = url;
		}

		@Override
		public Set<User> call() throws Exception {
			return crawlPage(url);
		}

	}
}
