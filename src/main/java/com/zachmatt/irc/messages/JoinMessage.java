package com.zachmatt.irc.messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zachmatt.irc.server.*;
import com.zachmatt.irc.exceptions.*;

public class JoinMessage extends Message {

    public JoinMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }

    public List<String> executeCommand(Server server, UserInfo user) {
        List<String> responses;

        if (parameters.size() == 0) {
            responses = super.generateResponse(ResponseCode.ERR_NEEDMOREPARAMS, user);
        }
        // If parameters are just "0", remove user from all their channels
        else if (parameters.size() == 1 && parameters.get(0).equals("0")) {
            responses = new ArrayList<String>();

            // Remove user from all channels
            List<Channel> channelsWithUser = server.getChannelsWithUser(user);
            for (Channel channel : channelsWithUser) {

                // Broadcast PART to all channel users
                for (UserInfo channelUser : channel.getUsers()) {
                    try {
                        channelUser.outStream.writeObject(
                                channel.getName() + " PART " + user.nickname);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                channel.removeUser(user);
            }
        }
        else if (parameters.size() == 1) {
            String[] channelNames = parameters.get(0).split(",");
            responses = new ArrayList<String>();

            for (String channelName : channelNames) {
                try {
                    Channel actualChannel = server.getChannelByName(channelName);

                    // Channel was found
                    actualChannel.addUser(user);

                    // Broadcast JOIN to all channel users
                    for (UserInfo channelUser : actualChannel.getUsers()) {
                        try {
                            channelUser.outStream.writeObject(
                                    channelName + " JOIN " + user.nickname);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // Respond with topic, or 'no topic is set'
                    if(actualChannel.getTopic().length() == 0) {
                        responses.add(channelName + " :No topic is set");
                    }
                    else {
                        responses.add(channelName + " :" +
                                actualChannel.getTopic());
                    }

                    // Respond with RPL_NAMEREPLY, listing all users on channel
                    StringBuilder sb = new StringBuilder();
                    sb.append(channelName + " :");

                    UserInfo[] users = actualChannel.getUsers()
                                                    .toArray(new UserInfo[0]);
                    sb.append(users[0].nickname);
                    for (int i = 1; i < users.length; i++) {
                        sb.append(" " + users[i].nickname);
                    }

                    responses.add(sb.toString());
                    responses.add(channelName + " :End of NAMES list");
                } catch (ChannelNotFoundException e) {
                    // Respond no such channel
                    responses.add(channelName + " :No such channel");
                }
            }
        }
        else { // Shouldn't end up here...respond with message or no?
            responses = new ArrayList<String>();
        }

        return responses;
    }
}
