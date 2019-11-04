// A Java program for a Client 
import java.net.*;
import java.io.*;

public class Client
{
    // initialize socket and input output streams 
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    // constructor to put ip address and port 
    public Client(String address, int port)
    {
        // establish a connection 
        try{
            socket = new Socket(address, port);
            System.out.println("Connected");

            //Input message
            input = new DataInputStream(System.in);
            //Message from server
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            //Writes to server
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        String line = "";
        while(!line.equals("End")) {
            try {
                line = input.readLine();
                out.writeUTF(line);  //Write to server
                while(!(line.endsWith("]") || line.endsWith("values") || line.endsWith("value"))) {
                    line = in.readUTF();
                    if(line.equals("End")){
                        break;
                    }
                    System.out.println(line);
                }
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        // close the connection 
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);
    }
} 