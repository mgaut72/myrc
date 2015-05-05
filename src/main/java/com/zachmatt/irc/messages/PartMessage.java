package com.zachmatt.irc.messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zachmatt.irc.server.*;
import com.zachmatt.irc.exceptions.*;

public class PartMessage extends Message {

    public PartMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }

    public List<String> executeCommand(Server server, UserInfo user) {
        if (!user.isRegistered()) {
            return super.generateResponse(ResponseCode.ERR_NOTREGISTERED, user);
        }

        List<String> responses;

        if (parameters.size() == 0) {
            responses = super.generateResponse(ResponseCode.ERR_NEEDMOREPARAMS, user);
        }
        else if (parameters.size() == 1) {
            String[] channelNames = parameters.get(0).split(",");
            responses = new ArrayList<String>();

            for (String channelName : channelNames) {
                try {
                    Channel actualChannel = server.getChannelByName(channelName);

                    // Channel was found
                    // Broadcast PART to all channel users
                    if (actualChannel.hasUser(user)) {
                        for (UserInfo channelUser : actualChannel.getUsers()) {
                            try {
                                String exitMessage = trailing.length() == 0 ?
                                        user.nickname : trailing;
                                channelUser.outStream.writeObject(
                                        channelName + " PART " + exitMessage);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        actualChannel.removeUser(user);
                    }
                    else {
                        responses.add(channelName + " :You're not on that channel");
                    }
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
