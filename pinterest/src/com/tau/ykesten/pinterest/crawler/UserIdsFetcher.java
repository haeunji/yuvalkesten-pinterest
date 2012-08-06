package com.tau.ykesten.pinterest.crawler;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserIdsFetcher {

	//private static final Pattern userIdPattern = Pattern.compile("\\s*\\<a\\shref=\"/([A-Za-z0-9]+)/\"\\sclass=\"ImgLink\"\\>|\\s*\\<a\\shref=\"/([A-Za-z0-9]+)/\"\\stitle=\"[^\"]+\"\\sclass=\"ImgLink\"\\>");


	public static Set<String> getUserIds(String url) {
		Iterable<String> responseLineByLine = UrlFetcher.getResponseLineByLine(url);
		return getUserIds(responseLineByLine);
	}


	public static Set<String> getUserIds(Iterable<String> responseLineByLine) {
		HashSet<String> result = new HashSet<String>();
		if (responseLineByLine == null){
			return result;
		}
		for (String line : responseLineByLine) {
			/*Matcher matcher = userIdPattern.matcher(line);
			if (matcher.matches()){
				result.add(matcher.group(1));
			}*/
			if (line.contains("class=\"ImgLink\"")){
				int start = line.indexOf("href=\"/");
				int end = line.indexOf("/\" ");
				String id = line.substring(start+7, end);
				result.add(id);
			}
		}
		return result;
	}
}
