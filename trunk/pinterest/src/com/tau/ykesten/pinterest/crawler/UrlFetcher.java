package com.tau.ykesten.pinterest.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UrlFetcher {
	
	// Create a trust manager that does not validate certificate chains
	private static final SSLSocketFactory sslSocketFactory = fixSslCerts();
	private static final long SLEEP_TIME_MILLI = 1000 * 4 + 100;//900 calls per hour => call every 4 seconds

	public static String getRawResponse(String url) throws IOException {
		InputStream stream = getStream(url);
		if (stream == null){
			return null;
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();
		String rawResponse = sb.toString();
		return rawResponse;
	}

	public static Iterable<String> getResponseLineByLine(String url) {
		InputStream stream = getStream(url);
		if (stream == null){
			return null;
		}
		final BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		final Iterator<String> iter = new Iterator<String>() {

			@Override
			public boolean hasNext() {
				try {
					return in.ready();
				} catch (IOException e) {
					return false;
				}
			}

			@Override
			public String next() {
				try {
					return in.readLine();
				} catch (IOException e) {
					return "";
				}
			}

			@Override
			public void remove() {
				// Do nothing
			}

		};
		return new Iterable<String>() {

			@Override
			public Iterator<String> iterator() {
				return iter;
			}

		};
	}

	private static InputStream getStream(String url) {
		if (sslSocketFactory == null) {
			return null;
		}

		try {
			// All set up, we can get a resource through https now:
			final URLConnection urlCon = new URL(url).openConnection();
			// Tell the url connection object to use our socket factory which bypasses
			// security checks
			if (urlCon instanceof HttpsURLConnection) {
				((HttpsURLConnection) urlCon).setSSLSocketFactory(sslSocketFactory);
				Thread.sleep(SLEEP_TIME_MILLI);//API is https, scraping is not
			}
			final InputStream input = urlCon.getInputStream();
			return input;
		} catch (final Exception e) {
			return null;
		}

	}

	private static SSLSocketFactory fixSslCerts() {
		final TrustManager[] trustAllCerts = new TrustManager[1];
		trustAllCerts[0] = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		// Install the all-trusting trust manager
		SSLContext sslContext;
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// Create an ssl socket factory with our all-trusting manager
		final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		return sslSocketFactory;
	}

}
