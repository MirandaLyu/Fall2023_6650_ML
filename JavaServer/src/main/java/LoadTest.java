import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class LoadTest {

  public static void main(String[] args) {
    if (args.length != 4) {
      System.out.println("Usage: java LoadTest <threadGroupSize> <numThreadGroups> <delay> <IPAddr>");
      System.exit(1);
    }

    int threadGroupSize = 10;
    int numThreadGroups = 30;
    int delay = 2;
    String serverUrl = "http://54.212.23.108:8080/assignment1_war";

    LoadTest loadTest = new LoadTest();
    loadTest.runLoadTest(threadGroupSize, numThreadGroups, delay, serverUrl);
  }

  public void runLoadTest(int threadGroupSize, int numThreadGroups, int delay, String serverUrl) {
    ExecutorService executorService = Executors.newFixedThreadPool(threadGroupSize * numThreadGroups);
    CountDownLatch initializationLatch = new CountDownLatch(threadGroupSize);



    // Initialize threads
//    for (int i = 0; i < 10; i++) {
//      executorService.submit(() -> {
//        try {
//          runApiCalls(100, serverUrl);
//        } catch (IOException e) {
//          throw new RuntimeException(e);
//        }
//        initializationLatch.countDown();
//      });
//    }

    // Wait for initialization threads to complete
//    try {
//      initializationLatch.await();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

//    System.out.println("Initialization phase completed.");

    // Create a connection manager with connection pooling settings
//    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//    connectionManager.setDefaultMaxPerRoute(20);  // Maximum connections per route
//    connectionManager.setMaxTotal(6000);           // Maximum total connections

    // Create a shared CloseableHttpClient using the connection manager
//    CloseableHttpClient httpClient = HttpClients.custom()
//        .setConnectionManager(connectionManager)
//        .build();

    long startTime = System.currentTimeMillis();

    // Start thread groups
    for (int i = 0; i < numThreadGroups; i++) {
      System.out.println("Starting Thread Group: " + (i + 1));

      // Start threadGroupSize threads
      for (int j = 0; j < threadGroupSize; j++) {
        // a thread begins
        executorService.submit(() -> {
          // create a httpClient for each thread and use it for all calls within
          try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            runApiCalls(1000, serverUrl, httpClient);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
      }

      // Wait for the specified delay
      try {
        TimeUnit.SECONDS.sleep(delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // Shutdown the executor service
    executorService.shutdown();

    try {
      // Wait for all threads to finish or a timeout to occur
      if (!executorService.awaitTermination(9, TimeUnit.MINUTES)) {
        // Handle the case where not all threads finished within the timeout
        System.out.println("Some threads did not finish within the timeout.");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Shutdown the shared httpClient and connection manager
//    try {
//      httpClient.close();
//      connectionManager.close();
//    } catch (IOException e) {
//      // Handle exceptions
//      e.printStackTrace();
//    }

    long endTime = System.currentTimeMillis();
    long wallTime = (endTime - startTime) / 1000; // in seconds
    long totalRequests = numThreadGroups * threadGroupSize * 2000; // total requests
    double throughput = (double) totalRequests / wallTime;

    System.out.println("Wall Time: " + wallTime + " seconds");
    System.out.println("Throughput: " + throughput + " requests per second");
  }

  private void runApiCalls(int numCalls, String serverUrl, CloseableHttpClient httpClient) throws IOException {
      AlbumClient albumClient = new AlbumClient();
      for (int i = 0; i < numCalls; i++) {
        try {
          albumClient.postAlbum(new File("/Users/echo/Downloads/nmtb.png"), "Artist", "Title",
              "2023", httpClient, serverUrl);
          albumClient.getAlbum("94f0f981-2d6f-4180-9813-e64ba3f4360f", httpClient, serverUrl);
        } catch (IOException e) {
          e.printStackTrace();
          // Handle the exception and retry up to 5 times
          for (int retryCount = 0; retryCount < 5; retryCount++) {
            try {
              TimeUnit.SECONDS.sleep(1);
              albumClient.postAlbum(new File("/Users/echo/Downloads/nmtb.png"), "Artist", "Title",
                  "2023", httpClient, serverUrl);
              albumClient.getAlbum("94f0f981-2d6f-4180-9813-e64ba3f4360f", httpClient, serverUrl);
              break;
            } catch (IOException | InterruptedException retryException) {
              retryException.printStackTrace();
            }
          }
        }
      }
    }
}

