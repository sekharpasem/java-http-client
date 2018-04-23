import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AppHttpClient {
	public void post(String uri, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException {

		if (uri == null || uri.isEmpty()) {
			throw new IllegalArgumentException("Uri or handler is missing.");
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse jsonResponse = null;
		try {
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			jsonResponse = httpclient.execute(httpPost);
			BufferedReader rd = new BufferedReader(new InputStreamReader(jsonResponse.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}

		} finally {
			httpclient.close();
		}
	}

	public JsonObject get(String uri, List<NameValuePair> nameValuePairs)
			throws ClientProtocolException, IOException, URISyntaxException {
		if (uri == null || uri.isEmpty()) {
			throw new IllegalArgumentException("Uri is missing");
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse jsonResponse = null;
		JsonObject returnData = null;
		JsonParser parser = new JsonParser();
		try {

			URIBuilder utiBuilder = new URIBuilder(uri);
			if (nameValuePairs != null) {
				for (NameValuePair pair : nameValuePairs) {
					utiBuilder.setParameter(pair.getName(), pair.getValue());
				}
			}
			HttpGet httpGet = new HttpGet(utiBuilder.build());
			jsonResponse = httpclient.execute(httpGet);
			/*
			 * // Get the response BufferedReader rd = new BufferedReader(new
			 * InputStreamReader(jsonResponse.getEntity().getContent()));
			 * 
			 * StringBuilder builder1 = new StringBuilder(); String line = ""; while ((line
			 * = rd.readLine()) != null) { builder1.append(line); }
			 * System.out.println(builder1);
			 */

			int status = jsonResponse.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = jsonResponse.getEntity();
				returnData = (JsonObject) parser.parse(EntityUtils.toString(entity));
				// ensure its fully consumed
				EntityUtils.consume(entity);
			} else {
				// handle 404 or Internal Server status
				throw new RuntimeException("Internal Server error while processing request");
			}
		} finally {
			httpclient.close();
		}
		return returnData;
	}
}
