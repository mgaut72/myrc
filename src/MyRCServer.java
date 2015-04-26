import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.HashMap;
import java.util.UUID;

public class MyRCServer implements Runnable {

    public static final int CONNECTION_PORT_NUMBER = 8000;
    private ServerSocket serverSocket;
    private HashMap<String,ObjectOutputStream> clients;
    private HashMap<String,Channel> channels;

    public static void main(String[] args) {
        MyRCServer server = new MyRCServer();
        Thread t = new Thread(server);
        t.start();
    }

    public MyRCServer() {
        try {
            serverSocket = new ServerSocket(CONNECTION_PORT_NUMBER);
            clients = new HashMap<String,ObjectOutputStream>();
            channels = new HashMap<String,Channel>();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket newSocket = serverSocket.accept();

                String clientID = UUID.randomUUID().toString();
                clients.put(clientID, new ObjectOutputStream(
                        newSocket.getOutputStream() ));
                ClientInteractionThread newConnection = new
                        ClientInteractionThread(this, newSocket);
                Thread t = new Thread(newConnection);
                t.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Channel getChannelByName(String name) throws ChannelNotFoundException {
        Channel result = channels.get(name);

        if (result == null) {
            throw new ChannelNotFoundException(
                    "Channel " + name + " does not exist");
        }
        else {
            return result;
        }
    }

    class ChannelNotFoundException extends Exception {

        public ChannelNotFoundException(String message) {
            super(message);
        }

    }

}
