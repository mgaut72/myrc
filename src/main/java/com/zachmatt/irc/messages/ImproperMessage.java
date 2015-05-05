package com.zachmatt.irc.messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zachmatt.irc.server.*;
import com.zachmatt.irc.exceptions.*;

public class ImproperMessage extends Message {

    public ImproperMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }

    public List<String> executeCommand(Server server, UserInfo user) {
        List<String> responses = new ArrayList<String>();
        responses.add(":Error, invalid message format");
        return responses;
    }
}
