import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AlbumClient {

  private static final String BASE_URL = "http://54.218.41.18:8080/assignment1_war";

  public static void main(String[] args) throws IOException {
    AlbumClient albumClient = new AlbumClient();
    // create httpClient connection here
    try (CloseableHttpClient httpClient = HttpClients.createDefault()){
      // Example GET request
//    String albumId = "98fb9da4-581e-469e-b65d-9bdb26a851a8";
//    albumClient.getAlbum(albumId);

//     Example POST request
      File imageFile = new File("/Users/echo/Downloads/nmtb.png");
      String artist = "Artist Name";
      String title = "Album Title";
      String year = "2023";
      albumClient.postAlbum(imageFile, artist, title, year, httpClient, BASE_URL);
    }
  }

  public void getAlbum(String albumId, CloseableHttpClient httpClient, String serverUrl) throws IOException {
      String url = serverUrl + "/albums/" + albumId;
      HttpGet httpGet = new HttpGet(url);

      HttpResponse response = httpClient.execute(httpGet);
      handleResponse(response);
  }

  public void postAlbum(File imageFile, String artist, String title, String year, CloseableHttpClient httpClient, String serverUrl) throws IOException {
      String url = serverUrl + "/albums";
      HttpPost httpPost = new HttpPost(url);

      // Construct multipart/form-data
      HttpEntity entity = MultipartEntityBuilder.create()
          .addBinaryBody("image", imageFile, ContentType.APPLICATION_OCTET_STREAM, imageFile.getName())
          .addTextBody("profile", buildProfileJson(artist, title, year), ContentType.APPLICATION_JSON)
          .build();

      httpPost.setEntity(entity);

      HttpResponse response = httpClient.execute(httpPost);
      handleResponse(response);
  }

  private void handleResponse(HttpResponse response) throws IOException {
    int statusCode = response.getStatusLine().getStatusCode();
    String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

//    System.out.println("Status Code: " + statusCode);
//    System.out.println("Response Body: " + responseBody);
  }

  private String buildProfileJson(String artist, String title, String year) {
    return String.format("{\"artist\": \"%s\", \"title\": \"%s\", \"year\": \"%s\"}", artist, title, year);
  }
}

