import Profiles.Dataprofile;
import java.io.*;
import java.util.*;

import Profiles.csvData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdk.nashorn.internal.objects.DataPropertyDescriptor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.print.attribute.standard.DateTimeAtCreation;

public class ConvertToProtobuf {
    /**
     * Takes the CSV file and get the records in the CSV. Store them in an array of type "CSV File"
     */
    public static List<csvData> readCSVFile(String filePath, int begin, int end) {
        BufferedReader fileReader = null;
        CSVParser csvParser = null;
        int counter = 0;

        List<csvData> dataProto = new ArrayList<csvData>();

        try {
            fileReader = new BufferedReader(new FileReader(filePath));
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                try {
                    if (counter >= begin) {
                        csvData record = new csvData(Double.parseDouble(csvRecord.get("CPUUtilization_Average")),
                                Double.parseDouble(csvRecord.get("NetworkIn_Average")),
                                Double.parseDouble(csvRecord.get("NetworkOut_Average")),
                                Double.parseDouble(csvRecord.get("MemoryUtilization_Average")));
                        dataProto.add(record);
                    }
                    /**Counter is here to initiate where to start reading
                     * and where to stop reading*/
                    counter++;
                    if(counter == end){
                        break;
                    }
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

        return dataProto;
    }

    public static void csvToProto(List<csvData> dataProto, String filePath) {

        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            for (int i = 0; i < dataProto.size(); i++){
                Dataprofile.Data.Builder protoProfile = Dataprofile.Data.newBuilder()
                        .setCpu(dataProto.get(i).getCPU())
                        .setNetworkIn(dataProto.get(i).getNetworkIn())
                        .setNetworkOut(dataProto.get(i).getNetworkOut())
                        .setMemory(dataProto.get(i).getMemory());


                //Dataprofile.Data protoProfileText = Dataprofile.Data.parseFrom();
                protoProfile.build().writeTo(outputStream);
            }

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String args[]){

        String currentDir = System.getProperty("user.dir");
        String csvData = currentDir + "\\src\\main\\java\\NDBench-testing.csv";
        String newFile = currentDir + "\\src\\BinaryFile.txt";

        csvToProto(readCSVFile(csvData,1,3),newFile);

    }


}
