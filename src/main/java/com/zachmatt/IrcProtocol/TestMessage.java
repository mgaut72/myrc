package com.zachmatt.IrcProtocol;

import java.util.ArrayList;
import java.util.List;

public class TestMessage extends Message {

    public TestMessage(String prefix, String command, List<String> params,
            String crlf) {
        super(prefix, command, params, crlf);
    }

    public TestMessage(String rawMessage) {
        super(rawMessage);
    }

    @Override
    public List<String> executeCommand(Server server, UserInfo user) {
        System.out.println("Woo test message works");
        List<String> responses = new ArrayList<String>();
        responses.add("Client should print this out");
        responses.add("Client should print this out too");
        return responses;
    }
}
