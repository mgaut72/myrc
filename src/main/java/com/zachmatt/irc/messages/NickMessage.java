package com.zachmatt.irc.messages;

import java.util.List;

import com.zachmatt.irc.server.*;

public class NickMessage extends Message {


    public NickMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }


    public List<String> executeCommand(Server server, UserInfo user) {
        return null;
    }
}
