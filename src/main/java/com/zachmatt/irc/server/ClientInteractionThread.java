package com.zachmatt.irc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.zachmatt.irc.messages.*;

public class ClientInteractionThread implements Runnable {

    private Server server;
    private Socket clientSocket;
    private UserInfo user;

    public ClientInteractionThread(Server server, Socket clientSocket,
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
                String clientInput = (String) fromClient.readObject();

                // Parse input to determine message type
                // For now...
                Message message = Message.generateMessage(clientInput);
                List<String> responses = message.executeCommand(server, user);

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
