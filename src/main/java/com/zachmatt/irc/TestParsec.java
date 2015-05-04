package com.zachmatt.irc;

import java.util.List;

import com.zachmatt.irc.messages.*;

public class TestParsec {

    public static void main(String [] args){

        String raw = ":<prefix> command params params params " +
            "params params params params params " +
            "params params params params params :trailing";

        raw = ":<prefix> command <param1> <param2> <param3> :<trailing>"; 
        Message msg = Message.generateMessage(raw);

        System.out.println("prefix: " + msg.getPrefix());
        System.out.println("command: " + msg.getCommand());
        System.out.println("params:");
        for(String p : msg.getParameters())
            System.out.println("\t" + p);
        System.out.println("trailing: " + msg.getTrailing());


    }
}
