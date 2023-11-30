package io.swagger.client;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Plotting {

  public static void main(String[] args) {
    String csvFilePath = "/Users/echo/Documents/NEU_courses/fall2023/6650/assignment1JavaClient/performance_data_Go30.csv";

    try {
      Map<String, Integer> throughputData = calculateThroughput(csvFilePath);
      try (FileWriter writer = new FileWriter("throughputPerSecond2.csv")) {
        // Write the CSV header
        writer.write("Second,Throughput\n");

        // Write data rows
        for (Map.Entry<String, Integer> entry : throughputData.entrySet()) {
          writer.write(entry.getKey() + "," + entry.getValue() + "\n");
        }

        System.out.println("CSV file created successfully.");
      } catch (IOException e) {
        e.printStackTrace();
      }


      for (Map.Entry<String, Integer> entry : throughputData.entrySet()) {
        System.out.println("Time: " + entry.getKey() + " seconds, Throughput: " + entry.getValue());
      }

    } catch (IOException | ParseException | CsvValidationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Calculate throughput at every second based on the CSV file.
   *
   * @param csvFilePath Path to the CSV file
   * @return A map with throughput data, where the key is the time in seconds and the value is the
   *     number of requests
   * @throws IOException
   * @throws ParseException
   * @throws CsvValidationException
   */
  private static Map<String, Integer> calculateThroughput(String csvFilePath)
      throws IOException, ParseException, CsvValidationException {
    Map<String, Integer> throughputData = new HashMap<>();

    try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
      // Skip the header row
      reader.readNext();
      String[] nextLine;
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");

      while ((nextLine = reader.readNext()) != null) {
        String startTimeString = nextLine[0]; // Assuming the "start time" column is at index 0
        // Parse the string into a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(startTimeString, formatter);
        // Create a label combining hour, minute, and second
        String label = String.format("%02d:%02d:%02d",
            dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());

        // Count requests for each second based on request type (you may want to filter based on request type)
        throughputData.put(label, throughputData.getOrDefault(label, 0) + 1);
      }
    }
    return throughputData;
  }
}
