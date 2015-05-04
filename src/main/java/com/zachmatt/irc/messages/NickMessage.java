package com.zachmatt.irc.messages;

import java.util.List;

import com.zachmatt.irc.server.*;

public class NickMessage extends Message {


    public NickMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }


    public List<String> executeCommand(Server server, UserInfo u) {
        String oldNick = u.nickname;

        // make sure we were given a nickname
        try{
            String newNick = this.params.get(0);
        }
        catch(IndexOutOfBoundsException e){
            return super.generateResponse(ResponseCode.ERR_NONICKNAMEGIVEN, u);
        }

        // make sure the new nickname doesn't already exist
        try{
            UserInfo existing = server.getUserByNickname(newNick);
            return super.generateResponse(ResponseCode.ERR_NICKNAMEINUSE, u);
        }
        catch(UserNotFoundException e){
            // expected to not find the user, so ok
        }


        // user must register and send PASS command first
        if(u.registrationState == UserInfo.RegistrationState.NONE_SENT){
            return super.generateResponse(ResponseCode.ERR_NOTREGISTERED, u);
        }
        // if they have never set a nickname, update their registration state
        else if(u.registrationState == UserInfo.RegistrationState.PASS_SENT){
            u.registrationState = UserInfo.RegistrationState.NICK_SENT;
        }

        // finally, set the new nickname
        u.nickname = newNick;
        return new ArrayList<String>() {{
            add(":" + oldNick + " NICK " + newNick);
        }};
    }
}
