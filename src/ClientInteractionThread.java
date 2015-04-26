import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientInteractionThread implements Runnable {

    private MyRCServer server;
    private Socket clientSocket;

    public ClientInteractionThread(MyRCServer server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(
                    clientSocket.getInputStream() );
            ObjectOutputStream toClient = new ObjectOutputStream(
                    clientSocket.getOutputStream() );

            while (true) {
                Object clientObject = fromClient.readObject();
                System.out.println("Got a message");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
