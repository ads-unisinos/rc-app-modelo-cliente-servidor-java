package M3InteracaoClienteServidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int counter = 0;

    public Server(){}

    public void runServer()
    {

        try {
            server = new ServerSocket( 12345, 100);

            while (true)
            {
                try {
                    waitForConnection();
                    getStreams();
                    processConnection();
                }
                catch (EOFException eof)
                {
                    displayMassage("\nServer terminated connection");
                }
                finally {
                    closeConnection();
                    counter++;
                }

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private void waitForConnection() throws IOException
    {
        displayMassage("Waiting for connection\n");
        connection = server.accept();
        displayMassage("Connection " + counter + "recieved from " +
                connection.getInetAddress().getHostAddress());
    }

    public void getStreams() throws IOException
    {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream( connection.getInputStream());
        displayMassage("\nGot the streams\n");
    }

    public void processConnection() throws IOException
    {
        String message = "Connection Successful";
        sendData(message);

        do{
            try {
                message = (String) input.readObject();
                displayMassage("\n " + message);
            } catch (ClassNotFoundException classNotFoundException)
            {
                displayMassage("\nUnknown object type received");
            }

        }while(!message.equals("CLIENT>>> TERMINATE"));
    }

    public void sendData( String message)
    {
        try {
            output.writeObject("SERVER>> " + message);
            output.flush();
        }
        catch (IOException e)
        {
            displayMassage("\nError writing object");
        }
    }

    public void closeConnection()
    {
        displayMassage("\nTerminating connection\n");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void displayMassage(String s)
    {
        System.out.println(s);
    }

}
