package com.zachmatt.IrcProtocol;

import java.util.List;

public abstract class Message {

    private String prefix;
    private String command;
    private List<String> params;
    private String crlf;

    public Message(String prefix, String command, List<String> params,
            String crlf) {
        this.prefix = prefix;
        this.command = command;
        this.params = params;
        this.crlf = crlf;
    }

    public abstract List<String> executeCommand(Server server, UserInfo user);
}
