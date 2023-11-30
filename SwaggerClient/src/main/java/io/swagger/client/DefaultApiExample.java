package io.swagger.client;

//import io.swagger.client.*;
//import io.swagger.client.auth.*;
//import io.swagger.client.model.*;
//import io.swagger.client.api.DefaultApi;
//
//import java.io.File;
//import java.util.*;
//
//public class DefaultApiExample {
//
//  public static void main(String[] args) {
//    ApiClient apiClient = new ApiClient();
//    apiClient.setBasePath("http://localhost:8082");
//    DefaultApi apiInstance = new DefaultApi();
//    apiInstance.setApiClient(apiClient);
//    String albumID = "f286a2a2-8eb2-4411-9a2e-b7a0b27364ae"; // String | path  parameter is album key to retrieve
//    try {
//      AlbumInfo result = apiInstance.getAlbumByKey(albumID);
//      System.out.println(result);
//    } catch (ApiException e) {
//      System.err.println("Exception when calling DefaultApi#getAlbumByKey");
//      e.printStackTrace();
//    }
//  }
//}
    import io.swagger.client.*;
    import io.swagger.client.auth.*;
    import io.swagger.client.model.*;
    import io.swagger.client.api.DefaultApi;

    import java.io.File;
    import java.util.*;

public class DefaultApiExample {

  public static void main(String[] args) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath("http://localhost:8080/assignment1_war_exploded");
    DefaultApi apiInstance = new DefaultApi();
    apiInstance.setApiClient(apiClient);
    File image = new File("/Users/echo/Downloads/nmtb.png"); // File |
    AlbumsProfile profile = new AlbumsProfile(); // AlbumsProfile |
    profile.setArtist("the Bollocks");
    profile.setTitle("Never Mind");
    profile.setYear("1983");
    try {
      apiInstance.newAlbum(image, profile);
//      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#newAlbum");
      e.printStackTrace();
    }
  }
}
