import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.HashMap;

public class MyRCServer implements Runnable {

    public static final int CONNECTION_PORT_NUMBER = 8000;
    private ServerSocket serverSocket;
    private HashMap<String,UserInfo> usersMap;
    private HashMap<String,Channel> channels;

    public static void main(String[] args) {
        MyRCServer server = new MyRCServer();
        Thread t = new Thread(server);
        t.start();
    }

    public MyRCServer() {
        try {
            serverSocket = new ServerSocket(CONNECTION_PORT_NUMBER);
            usersMap = new HashMap<String,UserInfo>();
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

                UserInfo newUser = new UserInfo(
                        new ObjectOutputStream( newSocket.getOutputStream() ));
                usersMap.put(newUser.id, newUser);
                ClientInteractionThread newConnection = new
                        ClientInteractionThread(this, newSocket, newUser);
                Thread t = new Thread(newConnection);
                t.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserByName(String name)
            throws UserNotFoundException {
        UserInfo result = usersMap.get(name);

        if (result == null) {
            throw new UserNotFoundException(
                    "User " + name + " does not exist");
        }
        else {
            return result;
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

    class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) { super(message); }
    }

    class ChannelNotFoundException extends Exception {
        public ChannelNotFoundException(String message) { super(message); }
    }

}
