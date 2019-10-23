// A Java program for a Server 
import java.net.*;
import java.io.*;
import java.util.*;

import Profiles.csvData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import static java.lang.Integer.valueOf;

public class Server
{
    //initialize socket and input stream 
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;

    // constructor with port 
    public Server(int port)
    {
        // starts server and waits for a connection 
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket 
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            String line = "";

            // reads message from client until "Over" is sent 
            while (!line.equals("Over"))
            {
                try
                {
                    line = in.readUTF();
                    String[] array = line.split(",");
                    if(array.length <= 6) {
                        if (array[0] != null && array[0].matches("\\d+")) {
                            System.out.println(array[0] + " is an integer");
                        } else {
                            System.out.println(array[0] + "Not integer");
                        }
                        if (array.length > 1 && array[1] != null && array[1].matches("(?i)dvd|ndbench")) {
                            System.out.println(array[1] + " is a good benchmark type");
                        } else {
                            System.out.println("Not a proper benchmark type");
                        }
                        if (array.length > 2 && array[2] != null && array[2].matches("(?i)cpu|networkin|networkout|memory")) {
                            System.out.println(array[2] + " is a good workload metric");
                        } else {
                            System.out.println("Not a proper workload metric");
                        }
                        if (array.length > 3 && array[3] != null && array[3].matches("\\d+")) {
                            System.out.println(array[3] + " is an integer");
                        } else {
                            System.out.println("Not integer");
                        }
                        if (array.length > 4 && array[4] != null && array[4].matches("\\d+")) {
                            System.out.println(array[4] + " is an integer");
                        } else {
                            System.out.println("Not integer");
                        }
                        if (array.length > 5 && array[5] != null && array[5].matches("\\d+")) {
                            System.out.println(array[5] + " is an integer");
                        } else {
                            System.out.println("Not integer");
                        }
                    }
                    else{
                        System.out.println("Too many parameters were inputted");
                    }
                    System.out.println(Arrays.toString(array));


                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");

            // close connection 
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    /*//int RFW_ID, String benchType, String workMetric, int batchUnit, int batchStart, int batchSize
    public static void read (String benchType){
        if (benchType == "dvd_training"){
            String dvd_training_path = Paths.get(System.getProperty("user.dir") + "\\src\\main\\java\\DVD-training.csv").toString();
            File file = new File(dvd_training_path);

            try {
                Scanner inputStream = new Scanner(file);
                inputStream.nextLine();     //Skips the first line of the document.
                while(inputStream.hasNextLine()){
                    String data = inputStream.nextLine();
                    String[] values = data.split(",");
                    System.out.println(values[0]);
                    *//**Parse it into a JSON format. Then serialize it.*//*
                }
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }*/

    /**Takes the CSV file and get the records in the CSV. Store them in an array of type "CSV File"*/
    public static List<csvData> readCSVFile(String filePath){
        BufferedReader fileReader = null;
        CSVParser csvParser = null;

        List<csvData> dataJSON = new ArrayList<csvData>();

        try {
            fileReader = new BufferedReader(new FileReader(filePath));
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvParser.getRecords()) {
                csvData record = new csvData(Integer.parseInt(csvRecord.get("CPU")), Integer.parseInt(csvRecord.get("networkIn")), Integer.parseInt(csvRecord.get("networkOut")), Integer.parseInt(csvRecord.get("memory")));
                dataJSON.add(record);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File cannot be found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the CSV!");
            e.printStackTrace();
        }finally {
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

    public static void csvToJSON(List<csvData> dataJSON, String filePath){
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

    public static void convertJSON(){
        String currentDir = System.getProperty("user.dir");
        String csvData = currentDir + "\\src\\main\\java\\DVD-testing.csv";
        File dvdTestingInputCSV = new File (csvData);
        File dvdTestingInputJSON = new File(currentDir + "\\DVD-training.csv");

    }

    public static void main(String args[])
    {
        //Server server = new Server(5000);

        String currentDir = System.getProperty("user.dir");
        String csvData = currentDir + "\\src\\main\\java\\DVD-testing.csv";
        String newFile = currentDir + "\\src\\newDoc.json";


        csvToJSON(readCSVFile(csvData), newFile);

    }
} 