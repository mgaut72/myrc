package com.zachmatt.IrcProtocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable {

    public static final String HOST_NAME = "localhost";

    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public static void main(String[] args) {
        try {
            Client client = new Client();
            Thread t = new Thread(client);
            t.start();

            client.readFromConsole();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public Client() throws IOException {
        connectToServer();
    }

    public void connectToServer() throws IOException {
        socket = new Socket(HOST_NAME, Server.CONNECTION_PORT_NUMBER);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }

    public void readFromConsole() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
 
        do {
            input = scanner.nextLine();

            try {
                outStream.writeObject(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!input.equals("exit"));
    }

    @Override
    public void run() {
        try {
            inStream = new ObjectInputStream( socket.getInputStream() );

            while (true) {
                String response = (String) inStream.readObject();
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
