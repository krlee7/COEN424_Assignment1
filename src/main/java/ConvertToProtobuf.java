import Profiles.Dataprofile;
import java.io.*;
import java.nio.file.Files;
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


    public static List<String> returnValues(String filePath, String benchmarkType, String testType, String metric, int batchUnit, int batchID, int batchSize) throws IOException {
        File originalFile = new File(filePath + "\\main\\java\\" + benchmarkType + "-" + testType + ".csv");
        String originalFilePath = filePath + "\\main\\java\\" + benchmarkType + "-" + testType + ".csv";
        String destinationFilePath = filePath + "BinaryFile.txt";

        List<String> sampleValues = new ArrayList<>();

        File destinationFile = new File(destinationFilePath);
        byte[] destinationByteFile = Files.readAllBytes(destinationFile.toPath());
        FileInputStream inputStream = new FileInputStream(destinationFilePath);
        int sampleStart = batchID*batchUnit - 1;
        int sampleEnd = (batchID + batchSize)*batchUnit - 1;



        /**Will check first if samples in the last partition that may not have the same number of batch units
         * NOT give error reading.*/

        /*if (sampleEnd/batchUnit <= originalFile.length()/batchUnit){
            csvToProto(readCSVFile(originalFilePath,sampleStart,sampleEnd),destinationFilePath);
        }
        else if (sampleEnd/batchUnit <= (originalFile.length()/batchUnit)+1){
            csvToProto(readCSVFile(originalFilePath,sampleStart,sampleEnd),destinationFilePath);
        }
        else{
            sampleValues.add("Outside of batch samples");
        }*/

        /**Serialize the file*/
        csvToProto(readCSVFile(originalFilePath,sampleStart,sampleEnd),destinationFilePath);

        //sampleValues.add(Double.toString(Dataprofile.Data.parseDelimitedFrom(inputStream).getCpu()));
        System.out.println((Dataprofile.Data.parseFrom(inputStream)));

        /**Deserialize the file*/
        for (int i = sampleStart; i < sampleEnd; i++) {
            try {

                //FileInputStream inputStream = new FileInputStream(destinationFilePath);

                switch (metric) {
                    case "cpu":
                        sampleValues.add(Double.toString(Dataprofile.Data.parseFrom(destinationByteFile).getCpu()));

                        break;
                    case "networkIn":
                        sampleValues.add(Double.toString(Dataprofile.Data.parseFrom(destinationByteFile).getNetworkIn()));
                        break;
                    case "networkOut":
                        sampleValues.add(Double.toString(Dataprofile.Data.parseFrom(destinationByteFile).getNetworkOut()));
                        break;
                    case "memory":
                        sampleValues.add(Double.toString(Dataprofile.Data.parseFrom(destinationByteFile).getMemory()));
                        break;
                    default:
                        break;
                }

            /*for (Dataprofile.Data profile: destinationByteFile){
                switch (metric){
                    case "cpu":
                        sampleValues.add(Double.toString(profile.getCpu()));
                        break;
                    case"networkIn":

                }
            }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**From the testing, it seems like we'd need to put the
         * deserialization part in a for loop.*/
        System.out.println(sampleValues);
        for (int i = 0; i < sampleValues.size(); i++){
            System.out.println(sampleValues.get(i));
            System.out.println(sampleValues.size());
        }
        return sampleValues;
    }

    public static void main(String args[]) throws IOException {

        /*String currentDir = System.getProperty("user.dir");
        String csvData = currentDir + "\\src";
        String metric = "cpu";
        String benchMarkType = "DVD";
        String testType = "training";
        int batchUnit = 1;
        int batchID = 2;
        int batchSize = 1;

        returnValues(csvData,benchMarkType,testType,metric,batchUnit,batchID,batchSize);*/

        String currentDir = System.getProperty("user.dir");
        String csvData = currentDir + "\\src\\main\\java\\DVD-testing.csv";
        String newFile = currentDir + "\\src\\BinaryFile.txt";
        csvToProto(readCSVFile(csvData,1,10),newFile);
    }


}
