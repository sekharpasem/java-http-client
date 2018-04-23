import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;

public class JavaHttpClient {

	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
		String uri = "https://jsonplaceholder.typicode.com/posts/1";
		// TODO Auto-generated method stub
		AppHttpClient httpClient = new AppHttpClient();
		//List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//nvps.add(new BasicNameValuePair("name", "sekhar"));
		JsonObject jsonObject = httpClient.get(uri, null);
		System.out.println(jsonObject);
	}

}
