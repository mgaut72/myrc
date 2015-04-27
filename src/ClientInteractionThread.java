import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientInteractionThread implements Runnable {

    private MyRCServer server;
    private Socket clientSocket;
    private UserInfo user;

    public ClientInteractionThread(MyRCServer server, Socket clientSocket,
            UserInfo user) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(
                    clientSocket.getInputStream() );
            ObjectOutputStream toClient = new ObjectOutputStream(
                    clientSocket.getOutputStream() );

            while (true) {
                Message clientMessage = (Message) fromClient.readObject();
                List<String> responses = clientMessage.executeCommand(server, user);

                for (String response : responses) {
                    toClient.writeObject(response);
                }
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
