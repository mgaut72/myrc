package com.zachmatt.irc.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.zachmatt.irc.exceptions.*;

public class Server implements Runnable {

    public static final int CONNECTION_PORT_NUMBER = 8000;
    private ServerSocket serverSocket;
    private HashMap<String,UserInfo> usersMap;
    private HashMap<String,Channel> channels;

    public static void main(String[] args) {
        Server server = new Server();
        Thread t = new Thread(server);
        t.start();
    }

    public Server() {
        try {
            serverSocket = new ServerSocket(CONNECTION_PORT_NUMBER);
            usersMap = new HashMap<String,UserInfo>();
            channels = new HashMap<String,Channel>();
            channels.put("#Sports", new Channel("#Sports"));
            channels.put("#Politics", new Channel("#Politics"));
            channels.put("#HackerNews", new Channel("#HackerNews"));
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
                usersMap.put(newUser.nickname, newUser);
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

    public UserInfo getUserByNickname(String nick)
            throws UserNotFoundException {
        UserInfo result = usersMap.get(nick);

        if (result == null) {
            throw new UserNotFoundException(
                    "User " + nick + " does not exist");
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

    public List<Channel> getChannelsWithUser(UserInfo user) {
        List<Channel> userChannels = new ArrayList<Channel>();

        for (Channel channel : channels.values()) {
            if (channel.hasUser(user)) {
                userChannels.add(channel);
            }
        }

        return userChannels;
    }

}
