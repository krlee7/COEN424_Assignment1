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
    private DataOutputStream out = null;

    // constructor with port 
    public Server(int port)
    {
        // starts server and waits for a connection 
        try{
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            //Message from client
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            //Write to client
            out = new DataOutputStream(socket.getOutputStream());
            String line = "";

            //Parameters to return to client
            int rfwID = 0;
            int lastBatchID = 0;
            List<String> samples = new ArrayList<>();
            List<String> listID = new ArrayList<>();
            boolean sameID = false;

            while(!line.equals("End")) {
                try {
                    line = in.readUTF();
                    if(line.equals("End")){
                        break;
                    }
                    line = line.replaceAll("\\s+", "");  //Remove whitespaces
                    String[] array = line.split(",");               //Delimit commas
                    if (array.length <= 7) {
                        if ((array[0] != null && array[0].matches("\\d+")) &&
                                (array.length > 1 && array[1] != null && array[1].matches("(?i)dvd|ndbench")) &&
                                (array.length > 2 && array[2] != null && array[2].matches("(?i)testing|training")) &&
                                (array.length > 3 && array[3] != null && array[3].matches("(?i)cpu|networkin|networkout|memory")) &&
                                (array.length > 4 && array[4] != null && array[4].matches("\\d+")) &&
                                (array.length > 5 && array[5] != null && array[5].matches("\\d+")) &&
                                (array.length > 6 && array[6] != null && array[6].matches("\\d+"))
                        ) {
                            RFW rfw = new RFW(array[0], array[1], array[2], array[3], array[4], array[5], array[6]);

                            if(!listID.isEmpty()){
                                for(int i = 0; i < listID.size(); i++){
                                    if(rfw.getId().equals(listID.get(i))){
                                        sameID = true;
                                        out.writeUTF("Not a unique id value");
                                        break;
                                    }
                                }
                                if(sameID == false)
                                    listID.add(rfw.getId());
                                else {
                                    sameID = false;
                                    continue;
                                }
                            }
                            else{
                                listID.add(rfw.getId());
                            }

                            String currentDir = System.getProperty("user.dir");
                            String newFile = currentDir + "\\src";

                            rfwID = Integer.parseInt(rfw.getId());
                            int batchID = Integer.parseInt(rfw.getBatchID());
                            int batchSize = Integer.parseInt(rfw.getBatchSize());
                            lastBatchID = batchID + batchSize;
                            samples = ReadJson.returnJson(newFile, rfw.getBenchmarkType(), rfw.getTestType(), rfw.getMetric(),
                                    Integer.parseInt(rfw.getBatchUnit()), Integer.parseInt(rfw.getBatchID()), Integer.parseInt(rfw.getBatchSize()));
                            out.writeUTF("RFW from client");
                            out.writeUTF("RFW ID: " + rfwID);
                            out.writeUTF("Last batch ID: " + lastBatchID);
                            if (!samples.isEmpty()) {
                                if (!samples.get(0).equals("Outside of batch sample values")) {
                                    out.writeUTF("Samples: " + samples);
                                }
                                else {
                                    out.writeUTF(samples.get(0));
                                }
                            }
                            System.out.println(Arrays.toString(array));
                        } else {
                            out.writeUTF("Incorrect paramater values");
                            System.out.println("Incorrect paramater values");
                        }

                    } else {
                        System.out.println("Too many parameters were inputted");
                    }


                } catch (IOException i) {
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