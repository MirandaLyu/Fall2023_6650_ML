import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class EnhancedLoadTest {

  private List<RequestRecord> requestRecords = Collections.synchronizedList(new ArrayList<>());
  private AlbumClient albumClient = new AlbumClient();

  public static void main(String[] args) {
    if (args.length != 4) {
      System.out.println("Usage: java LoadTest <threadGroupSize> <numThreadGroups> <delay> <IPAddr>");
      System.exit(1);
    }

    int threadGroupSize = Integer.parseInt(args[0]);
    int numThreadGroups = Integer.parseInt(args[1]);
    int delay = Integer.parseInt(args[2]);
    String serverUrl = args[3];

    EnhancedLoadTest loadTest = new EnhancedLoadTest();
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
//
//    // Wait for initialization threads to complete
//    try {
//      initializationLatch.await();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//
//    System.out.println("Initialization phase completed.");

    // Start thread groups
    for (int i = 0; i < numThreadGroups; i++) {
      System.out.println("Starting Thread Group: " + (i + 1));

      // Start threadGroupSize threads
      for (int j = 0; j < threadGroupSize; j++) {
        executorService.submit(() -> {
          try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
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

    // Wait for all threads to complete
    try {
      executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    generatePerformanceStatistics();
  }

  private void runApiCalls(int numCalls, String serverUrl, CloseableHttpClient httpClient) throws IOException {
      for (int i = 0; i < numCalls; i++) {
        try {
          Instant startTime = Instant.now();
          albumClient.postAlbum(new File("/Users/echo/Downloads/nmtb.png"), "Artist", "Title",
              "2023", httpClient, serverUrl);
          Instant endTime = Instant.now();
          long latency = Duration.between(startTime, endTime).toMillis();
          requestRecords.add(new RequestRecord(startTime, "POST", latency, "200"));

          startTime = Instant.now();
          albumClient.getAlbum("44e794cc-0504-47d8-a641-6dc74fe54344", httpClient, serverUrl);
          endTime = Instant.now();
          latency = Duration.between(startTime, endTime).toMillis();
          requestRecords.add(new RequestRecord(startTime, "GET", latency, "200"));
        } catch (IOException e) {
          handleException(e, httpClient, serverUrl);
       }
      }
    }

  private void handleException(IOException e, CloseableHttpClient httpClient, String serverUrl) {
    e.printStackTrace();
    // Handle the exception and retry up to 5 times
    for (int retryCount = 0; retryCount < 5; retryCount++) {
      try {
        TimeUnit.SECONDS.sleep(1);
        Instant startTime = Instant.now();
        albumClient.postAlbum(new File("/Users/echo/Downloads/nmtb.png"), "Artist", "Title", "2023", httpClient, serverUrl);
        Instant endTime = Instant.now();
        long latency = Duration.between(startTime, endTime).toMillis();
        requestRecords.add(new RequestRecord(startTime, "POST", latency, "200"));

        startTime = Instant.now();
        albumClient.getAlbum("44e794cc-0504-47d8-a641-6dc74fe54344", httpClient, serverUrl);
        endTime = Instant.now();
        latency = Duration.between(startTime, endTime).toMillis();
        requestRecords.add(new RequestRecord(startTime, "GET", latency, "200"));
        break;
      } catch (IOException | InterruptedException retryException) {
        retryException.printStackTrace();
      }
    }
  }

  private void generatePerformanceStatistics() {
    // Calculate and display statistics
    // Mean, median, p99, min, max response time for POST
    calculateAndDisplayStatistics("POST");
    // Mean, median, p99, min, max response time for GET
    calculateAndDisplayStatistics("GET");

    // Write performance data to CSV file
    writePerformanceDataToCSV();
  }

  private void calculateAndDisplayStatistics(String requestType) {
    List<Long> latencies = new ArrayList<>();
    for (RequestRecord record : requestRecords) {
      if (record.getRequestType().equals(requestType)) {
        latencies.add(record.getLatency());
      }
    }

    // Calculate statistics
    double meanResponseTime = latencies.stream().mapToLong(Long::longValue).average().orElse(0);
    Collections.sort(latencies);
    double medianResponseTime = latencies.get(latencies.size() / 2);
    double p99ResponseTime = latencies.get((int) (latencies.size() * 0.99));
    long minResponseTime = latencies.stream().mapToLong(Long::longValue).min().orElse(0);
    long maxResponseTime = latencies.stream().mapToLong(Long::longValue).max().orElse(0);

    // Display statistics
    System.out.println("Statistics for " + requestType + " requests:");
    System.out.println("Mean Response Time: " + meanResponseTime + " milliseconds");
    System.out.println("Median Response Time: " + medianResponseTime + " milliseconds");
    System.out.println("P99 Response Time: " + p99ResponseTime + " milliseconds");
    System.out.println("Min Response Time: " + minResponseTime + " milliseconds");
    System.out.println("Max Response Time: " + maxResponseTime + " milliseconds");
  }

  private void writePerformanceDataToCSV() {
    try (FileWriter writer = new FileWriter("performance_data.csv")) {
      writer.write("Start Time,Request Type,Latency,Response Code\n");
      for (RequestRecord record : requestRecords) {
        writer.write(record.getStartTime() + "," + record.getRequestType() + "," + record.getLatency() + "," + record.getResponseCode() + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class RequestRecord {
    private Instant startTime;
    private String requestType;
    private long latency;
    private String responseCode;

    public RequestRecord(Instant startTime, String requestType, long latency, String responseCode) {
      this.startTime = startTime;
      this.requestType = requestType;
      this.latency = latency;
      this.responseCode = responseCode;
    }

    public Instant getStartTime() {
      return startTime;
    }

    public String getRequestType() {
      return requestType;
    }

    public long getLatency() {
      return latency;
    }

    public String getResponseCode() {
      return responseCode;
    }
  }
}


