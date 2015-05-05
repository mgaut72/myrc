package com.zachmatt.irc.messages;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import com.zachmatt.irc.server.*;
import com.zachmatt.irc.exceptions.*;

public class PrivmsgMessage extends Message {


    public PrivmsgMessage(String prefix, String command, List<String> params,
            String trailing) {
        super(prefix, command, params, trailing);
    }


    public List<String> executeCommand(Server server, final UserInfo u) {

        List<String> responses = new ArrayList<String>();

        // if prefix is populated, it should be the sender's username
        String msg = this.prefix + ": " + this.trailing;

        // parameters.get(0) should contain targets
        if(this.parameters.size() == 0){
            return super.generateResponse(ResponseCode.ERR_NEEDMOREPARAMS, u);
        }

        for(String target : this.parameters.get(0).split(",")){
            List<String> errParams = new ArrayList<String>();
            // case: target is a channel
            if(target.startsWith("#")) {
                try{
                    Channel chan = server.getChannelByName(target);
                    for(UserInfo chanMember : chan.getUsers()){
                        chanMember.outStream.writeObject(msg);
                    }
                }
                catch(ChannelNotFoundException e){
                    errParams.add(target);
                    responses.addAll(super.generateResponse(
                                ResponseCode.ERR_NOSUCHCHANNEL, u, errParams));
                }
                catch(IOException e){
                    responses.addAll(super.generateResponse(
                                ResponseCode.RPL_TRYAGAIN, u));
                }

            }
            // case: target is a user
            else{
                try{
                    UserInfo targetUser = server.getUserByNickname(target);
                    targetUser.outStream.writeObject(msg);
                }
                catch(UserNotFoundException e){
                    errParams.add(target);
                    responses.addAll(super.generateResponse(
                                ResponseCode.ERR_NOSUCHNICK, u, errParams));
                }
                catch(IOException e){
                    responses.addAll(super.generateResponse(
                                ResponseCode.RPL_TRYAGAIN, u));
                }
            }
        }// end for loop over targets

        return responses;
    }
}
