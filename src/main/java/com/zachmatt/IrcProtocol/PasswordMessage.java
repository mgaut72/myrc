package com.zachmatt.IrcProtocol;

import java.util.ArrayList;
import java.util.List;

public class PasswordMessage extends Message {

    public PasswordMessage(String prefix, String command, List<String> params,
            String crlf) {
        super(prefix, command, params, crlf);
    }

    public PasswordMessage(String rawMessage) {
        super(rawMessage);
    }

    @Override
    public List<String> executeCommand(Server server, UserInfo user) {
        List<String> responses;
        if (user.registrationState == UserInfo.RegistrationState.REGISTERED) {
            responses = super.generateResponse(ResponseCode.ERR_ALREADYREGISTERED, user);
        }
        else if (parameters.size() < 1) {
            responses = super.generateResponse(ResponseCode.ERR_NEEDMOREPARAMS, user);
        }
        else {
            if (user.registrationState == UserInfo.RegistrationState.NONE_SENT) {
                user.registrationState = UserInfo.RegistrationState.PASS_SENT;
            }
            // TODO: Generate reply to send back?
            responses = new ArrayList<String>();
        }
        return responses;
    }
}
