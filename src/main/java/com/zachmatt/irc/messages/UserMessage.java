package com.zachmatt.irc.messages;

import java.util.List;
import java.util.ArrayList;

import com.zachmatt.irc.server.*;
import com.zachmatt.irc.exceptions.UserNotFoundException;

public class UserMessage extends Message {


    public UserMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }


    public List<String> executeCommand(Server server, final UserInfo u) {
        List<String> errorParams = new ArrayList<String>();

        // expect params to be [username, hostname, servername]
        // and trailing to be realname
        if(this.parameters.size() < 3){
            return super.generateResponse(ResponseCode.ERR_NEEDMOREPARAMS, u);
        }

        final String oldNick = u.nickname;
        final String newNick = this.parameters.get(0);
        final String newRealName = this.trailing;

        // make sure the new nickname doesn't already exist
        try{
            UserInfo existing = server.getUserByNickname(newNick);
            errorParams.add(newNick);
            return super.generateResponse(
                    ResponseCode.ERR_NICKNAMEINUSE, u, errorParams);
        }
        catch(UserNotFoundException e){
            // expected to not find the user, so ok
        }

        // user must register and send PASS command first
        if(u.registrationState == UserInfo.RegistrationState.NONE_SENT){
            return super.generateResponse(ResponseCode.ERR_NOTREGISTERED, u);
        }
        // if they have never set a nickname, update their registration state
        else if(u.registrationState == UserInfo.RegistrationState.PASS_SENT
                || u.registrationState == UserInfo.RegistrationState.NICK_SENT){
            u.registrationState = UserInfo.RegistrationState.REGISTERED;
        }
        else {
            return super.generateResponse(ResponseCode.ERR_ALREADYREGISTERED, u);
        }

        // finally, set the new nickname and realname
        u.nickname = newNick;
        u.realname = newRealName;
        return new ArrayList<String>() {{
            add(":" + oldNick + " NICK " + newNick);
            add(":" + u.nickname + " realname = " + u.realname);
        }};
    }
}
