package com.zachmatt.irc.messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zachmatt.irc.server.*;
import com.zachmatt.irc.exceptions.*;

public class TopicMessage extends Message {

    public TopicMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }

    public List<String> executeCommand(Server server, UserInfo user) {
        if (!user.isRegistered()) {
            return super.generateResponse(ResponseCode.ERR_NOTREGISTERED, user);
        }

        List<String> responses;

        if (parameters.size() == 0) { // Invalid
            responses = super.generateResponse(ResponseCode.ERR_NEEDMOREPARAMS, user);
        }
        else if (parameters.size() == 1 && trailing.length() == 0) { // Retrieve topic of a channel
            responses = new ArrayList<String>();
            String channelName = parameters.get(0);

            try {
                Channel channel = server.getChannelByName(channelName);

                // Channel was found
                // Check if user belongs to channel
                if (channel.hasUser(user)) {
                    if (channel.getTopic().length() == 0) {
                        responses.add(channelName + " :No topic is set");
                    }
                    else {
                        responses.add(channelName + " :" + channel.getTopic());
                    }
                }
                else {
                    responses.add(channelName + " :You're not on that channel");
                }
            } catch (ChannelNotFoundException e) {
                // Respond no such channel
                responses.add(channelName + " :No such channel");
            }
        }
        else if (parameters.size() == 1 && trailing.length() > 0) { // Set topic on a channel
            responses = new ArrayList<String>();
            String channelName = parameters.get(0);
            String topic = trailing;

            try {
                Channel channel = server.getChannelByName(channelName);

                // Channel was found
                // Check if user belongs to channel
                if (channel.hasUser(user)) {
                    channel.setTopic(topic);
                    String broadcastMessage;
                    if (channel.getTopic().length() == 0) {
                        broadcastMessage = channelName + " TOPIC :No topic is set";
                    }
                    else {
                        broadcastMessage = channelName + " TOPIC :" + channel.getTopic();
                    }

                    // Broadcast TOPIC to all channel users
                    for (UserInfo channelUser : channel.getUsers()) {
                        try {
                            channelUser.outStream.writeObject(broadcastMessage);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    responses.add(channelName + " :You're not on that channel");
                }
            } catch (ChannelNotFoundException e) {
                // Respond no such channel
                responses.add(channelName + " :No such channel");
            }
        }
        else { // Invalid
            responses = new ArrayList<String>();
        }

        return responses;
    }
}
