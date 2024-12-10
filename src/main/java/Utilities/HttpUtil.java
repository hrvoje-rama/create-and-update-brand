package Utilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtil {
    private static final HttpClient client = HttpClients.createDefault();

    /**
     * Sends a GET request to the specified URL and returns the HttpResponse.
     *
     * @param url The URL to send the GET request to.
     * @return HttpResponse object containing the response.
     * @throws IOException if an I/O exception occurs.
     */
    public static HttpResponse sendGetRequest(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet getRequest = new HttpGet(url);
        return client.execute(getRequest);
    }

    /**
     * Creates an HTTP POST request with a JSON body to the specified URL.
     *
     * @param url The URL to send the POST request to.
     * @param jsonBody The JSON body to include in the POST request.
     * @return HttpPost object representing the configured POST request.
     * @throws IOException if an I/O exception occurs.
     */
    public static HttpPost createPostRequest(String url, String jsonBody) throws IOException {
        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(jsonBody));
        return postRequest;
    }

    /**
     * Sends a PUT request with a JSON body to the specified URL and returns the HttpResponse.
     *
     * @param url The URL to send the PUT request to.
     * @param jsonBody The JSON body to include in the PUT request.
     * @return HttpResponse object containing the response.
     * @throws IOException if an I/O exception occurs.
     */
    public static HttpResponse sendPutRequest(String url, String jsonBody) throws IOException {
        HttpPut putRequest = new HttpPut(url);
        putRequest.setHeader("Content-Type", "application/json");
        putRequest.setEntity(new StringEntity(jsonBody));
        return client.execute(putRequest);
    }

    /**
     * Extracts and returns the response body as a String from an HttpResponse.
     *
     * @param response The HttpResponse from which to extract the body.
     * @return The response body as a String.
     * @throws IOException if an I/O exception occurs.
     */
    public static String getResponseBody(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }
}
