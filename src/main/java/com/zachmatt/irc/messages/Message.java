package com.zachmatt.irc.messages;

import java.util.List;

import org.codehaus.jparsec.*;
import org.codehaus.jparsec.functors.*;
import org.codehaus.jparsec.pattern.*;

import com.zachmatt.irc.server.*;

public abstract class Message {

    protected String prefix;
    protected String command;
    protected List<String> parameters;
    protected String trailing;

    public Message(String prefix, String cmd, List<String> prms, String trail){
        this.prefix = prefix;
        this.command = command;
        this.parameters = prms;
        this.trailing = trail;
    }

    public Message(String rawMessage){

        /*
         * A mapper class that concatenates a list of strings
         */
        Map<List<String>, String> flat = new Map<List<String>, String>() {
            @Override
            public String map(List<String> ss) {
                StringBuilder sb = new StringBuilder();
                for (String s : ss){
                    sb.append(s);
                }
                return sb.toString();
            }
        };

        /*
         * mapper to combine 2 strings into 1
         */
        Map2<String, String, String> strPlus
            = new Map2<String, String, String>(){
                @Override
                public String map(String s1, String s2){ return s1 + s2; }
            };

        Parser<String> nospcrlfcl = Scanners.notAmong("\0\r :").source();

        Parser<String> middle = Parsers.sequence(
                nospcrlfcl,
                nospcrlfcl.or(Scanners.isChar(':').source()).many().map(flat),
                strPlus
                );

        Parser<String> trailing =
            nospcrlfcl.or(Scanners.among(": ").source()).many().map(flat);

        Parser<Pair<List<String>, String>> maxParams =
            Parsers.tuple(
                    Scanners.isChar(' ').next(middle).times(14),
                    Scanners.string(" :").atomic().or(Scanners.isChar(' '))
                            .next(trailing).optional(""));


        Parser<Pair<List<String>, String>> nonMaxParams =
            Parsers.tuple(
                    Scanners.isChar(' ').next(middle).atomic().times(0,14),
                    Scanners.string(" :").next(trailing).optional(""));

        Parser<Pair<List<String>,String>> params =
            Parsers.or(maxParams.atomic(), nonMaxParams);

        Parser<String> command = Parsers.or(
                Scanners.isChar(CharPredicates.IS_ALPHA)
                        .source().atLeast(1).map(flat),
                Scanners.isChar(CharPredicates.IS_DIGIT)
                        .source().times(3).map(flat));

        Parser<String> prefixBody = nospcrlfcl.many().map(flat);

        Parser<String> prefix = prefixBody.between(Scanners.isChar(':'),
                Scanners.isChar(' ')).atomic().optional("");

        Parser<Tuple3<String,String,Pair<List<String>,String>>> message
            = Parsers.tuple(prefix, command, params);

        Tuple3<String,String,Pair<List<String>,String>> parts
            = message.parse(rawMessage);

        this.prefix = parts.a;
        this.command = parts.b;
        this.parameters = parts.c.a;
        this.trailing = parts.c.b;
    }

    public String getPrefix(){
        return prefix;
    }

    public String getCommand(){
        return command;
    }

    public String getTrailing(){
        return trailing;
    }

    public List<String> getParameters(){
        return parameters;
    }

    // Create responses based on the code and the user info
    // Since this an instance method, we still have access to this message's
    // information such as prefix, command, etc.
    public List<String> generateResponse(ResponseCode rc, UserInfo user) {
        ArrayList<String> responses = new ArrayList<String>();
        switch(rc){
            case ResponseCode.ERR_ALREADYREGISTERED:
            case ResponseCode.ERR_NEEDMOREPARAM:
            case ResponseCode.ERR_REST:
            case ResponseCode.ERR_NONICKNAMEGIVEN:
                    responses.add(":No nickname given");
            case ResponseCode.ERR_NICKNAMEINUSE: // TODO get <nick>
                    responses.add("<nick> :Nickname is already in use");
            case ResponseCode.ERR_NOTREGISTERED:
                    responses.add(":You have not registered");
            default: break;
        }
        return responses;
    }

    public abstract List<String> executeCommand(Server server, UserInfo user);
}
