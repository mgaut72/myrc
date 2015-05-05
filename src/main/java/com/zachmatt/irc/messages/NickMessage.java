package com.zachmatt.irc.messages;

import java.util.List;
import java.util.ArrayList;

import com.zachmatt.irc.server.*;
import com.zachmatt.irc.exceptions.UserNotFoundException;

public class NickMessage extends Message {


    public NickMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }


    public List<String> executeCommand(Server server, UserInfo u) {
        final String oldNick = u.nickname;
        List<String> errorParams = new ArrayList<String>();

        // make sure we were given a nickname
        final String newNick;
        try{
            newNick = this.parameters.get(0);
        }
        catch(IndexOutOfBoundsException e){
            return super.generateResponse(ResponseCode.ERR_NONICKNAMEGIVEN, u);
        }

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
        else if(u.registrationState == UserInfo.RegistrationState.PASS_SENT){
            u.registrationState = UserInfo.RegistrationState.NICK_SENT;
        }

        u.nickname = newNick;

        // first time this user is setting a nickname, add them to map
        if(oldNick == null){
            server.addToUserMap(u);
        }
        // update the users key in the map
        else{
            server.updateUserNick(oldNick, newNick);
        }

        // finally, set the new nickname
        return new ArrayList<String>() {{
            add(":" + oldNick + " NICK " + newNick);
        }};
    }
}
