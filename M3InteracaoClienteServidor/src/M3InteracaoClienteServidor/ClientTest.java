package M3InteracaoClienteServidor;

public class ClientTest
{
    public static void main(String[] args) {
        Client application;

        if (args.length == 0)
            application = new Client("170.0.0.1");
        else
            application = new Client( args[0]);

        application.runClient();
    }
}
