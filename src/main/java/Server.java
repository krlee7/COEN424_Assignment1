// A Java program for a Server 
import java.net.*;
import java.io.*;
import java.util.*;


public class Server
{
    //initialize socket and input stream 
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

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
                    line = line.replaceAll("\\s+","");  //Remove whitespaces
                    String[] array = line.split(",");
                    if(array.length <= 7) {
                        if((array[0] != null && array[0].matches("\\d+")) &&
                                (array.length > 1 && array[1] != null && array[1].matches("(?i)dvd|ndbench")) &&
                                (array.length > 2 && array[2] != null && array[2].matches("(?i)testing|training")) &&
                                (array.length > 3 && array[3] != null && array[3].matches("(?i)cpu|networkin|networkout|memory")) &&
                                (array.length > 4 && array[4] != null && array[4].matches("\\d+")) &&
                                (array.length > 5 && array[5] != null && array[5].matches("\\d+")) &&
                                (array.length > 6 && array[6] != null && array[6].matches("\\d+"))
                        ){
                            RFW rfw = new RFW(array[0], array[1], array[2], array[3], array[4], array[5], array[6]);
                            String currentDir = System.getProperty("user.dir");
                            String newFile = currentDir + "\\src";

                            ReadJson.returnJson(newFile, rfw.getBenchmarkType(), rfw.getTestType(), rfw.getMetric(),
                                    Integer.parseInt(rfw.getBatchUnit()), Integer.parseInt(rfw.getBatchID()), Integer.parseInt(rfw.getBatchSize()));
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


    public static void main(String args[])
    {
        Server server = new Server(5000);


    }
} 