import java.io.*;
import java.util.*;

import Profiles.csvData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ConvertToJson {
    /**
     * Takes the CSV file and get the records in the CSV. Store them in an array of type "CSV File"
     */
    public static List<csvData> readCSVFile(String filePath) {
        BufferedReader fileReader = null;
        CSVParser csvParser = null;

        List<csvData> dataJSON = new ArrayList<csvData>();

        try {
            fileReader = new BufferedReader(new FileReader(filePath));
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                try {
                    csvData record = new csvData(Double.parseDouble(csvRecord.get("CPUUtilization_Average")),
                            Double.parseDouble(csvRecord.get("NetworkIn_Average")),
                            Double.parseDouble(csvRecord.get("NetworkOut_Average")),
                            Double.parseDouble(csvRecord.get("MemoryUtilization_Average")));
                    dataJSON.add(record);
                } catch (NumberFormatException e) {
                    System.out.println("Not a number " + e.getMessage());
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("File cannot be found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the CSV!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvParser.close();
            } catch (IOException e) {
                System.out.println("Closing fileRead or csvReader error.");
                e.printStackTrace();
            }
        }

        return dataJSON;
    }

    public static void csvToJSON(List<csvData> dataJSON, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);

        try {
            mapper.writeValue(file, dataJSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void convertJSON() {
        String currentDir = System.getProperty("user.dir");
        String csvData = currentDir + "\\src\\main\\java\\DVD-testing.csv";
        File dvdTestingInputCSV = new File(csvData);
        File dvdTestingInputJSON = new File(currentDir + "\\DVD-training.csv");

    }

    public static void main(String args[]) {

        String currentDir = System.getProperty("user.dir");
        String csvData = currentDir + "\\src\\main\\java\\NDBench-testing.csv";
        String newFile = currentDir + "\\src\\NDBench_testing.json";

        csvToJSON(readCSVFile(csvData), newFile);
    }
}