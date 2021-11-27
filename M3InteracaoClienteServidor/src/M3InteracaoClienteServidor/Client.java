package M3InteracaoClienteServidor;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client
{
    public ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private final String chatServer;
    private Socket client;

    public Client (String host)
    {
        chatServer = host;
    }

    public void runClient()
    {
        try {
            connectToServer();
            getStreams();
            processConnection();
        }
        catch (EOFException eof)
        {
            displayMassage("\nClient terminated connection");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    private void connectToServer() throws IOException
    {
        displayMassage("Attenting a connection\n");

        client = new Socket(InetAddress.getByName(chatServer), 12345);
        displayMassage("Connected to: " + client.getInetAddress().getHostName());
    }

    public void getStreams() throws IOException
    {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();

        input = new ObjectInputStream( client.getInputStream());
        displayMassage("\nGot the streams\n");
    }

    public void processConnection() throws IOException
    {
        do{
            try {
                message = (String) input.readObject();
                displayMassage("\n " + message);
            } catch (ClassNotFoundException classNotFoundException)
            {
                displayMassage("\nUnknown object type received");
            }

        }while(!message.equals("SERVER>>> TERMINATE"));
    }

    public void sendData( String message)
    {
        try {
            output.writeObject("CLIENT>>> " + message);
            output.flush();
            displayMassage("\nCLIENT>>> " + message);
        }
        catch (IOException e)
        {
            displayMassage("\nError writing object");
        }
    }

    public void closeConnection()
    {
        displayMassage("\nClosing connection\n");
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void displayMassage(String s)
    {
        System.out.println(s);
    }
}
