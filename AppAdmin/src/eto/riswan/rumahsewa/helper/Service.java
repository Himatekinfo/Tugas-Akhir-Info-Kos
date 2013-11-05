package eto.riswan.rumahsewa.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import eto.riswan.rumahsewa.core.Parameter;
import eto.riswan.rumahsewa.core.Parameter.ParameterType;

public class Service {
	public static StringBuilder inputStreamToString(InputStream is) throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null)
			total.append(line);

		// Return full string
		return total;
	}

	public static HttpResponse makeRequest(String url, List<Parameter> params) throws Exception {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for (Parameter p : params)
			if (p.type == ParameterType.STRING) {
				StringBody sb = new StringBody(p.value, ContentType.TEXT_PLAIN);
				builder.addPart(p.name, sb);
			} else if (p.type == ParameterType.BINARY) {
				FileBody fb = new FileBody(new File(p.value));
				builder.addPart(p.name, fb);
			}

		httppost.setEntity(builder.build());

		HttpResponse response = httpclient.execute(httppost);
		return response;
	}

	public static InputStream retrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("Service.InputStream", "Error " + statusCode + " for URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} catch (IOException e) {
			getRequest.abort();
			e.printStackTrace();
			Log.e("Service.InputStream", "Error for URL " + url, e);
		}

		return null;
	}
}
