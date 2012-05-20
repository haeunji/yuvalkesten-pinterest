package com.tau.ykesten.pinterest.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class PinterestAuthenticator {

	public String getToken(String userId) {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://api.pinterest.com/v2/oauth/access_token");

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("client_id", "1234567"));
			nameValuePairs.add(new BasicNameValuePair("client_secret", "ab04920a718c1e35ae5b08ffa4603dd62ef7c8fc"));
			nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
			nameValuePairs.add(new BasicNameValuePair("scope", "read_write"));
			nameValuePairs.add(new BasicNameValuePair("redirect_uri", "http://pinterest.com/about/iphone/"));

			post.addHeader("Host", "api.pinterest.com");
			post.addHeader("User-Agent", "Pinterest For iPhone / 1.4.3");
			post.addHeader("Content-Length", "96");
			post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			post.addHeader("Authorization", "Basic");
			post.addHeader("Accept-Encoding", "gzip");
			post.addHeader("Connection", "close");
			
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}
	
	public static void main(String[] args) {
		PinterestAuthenticator authenticator = new PinterestAuthenticator();
		authenticator.getToken("yuvalkesten");
	}
}
