package com.zachmatt.irc.messages;

import java.util.List;
import java.util.ArrayList;

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
        this.command = cmd;
        this.parameters = prms;
        this.trailing = trail;
    }

    public static Message generateMessage(String rawMessage){

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

        String          prfx    = parts.a;
        String          cmd     = parts.b;
        List<String>    prms    = parts.c.a;
        String          trail   = parts.c.b;

        Message msg;
        switch(cmd){
            case "PASS":
                msg = new PasswordMessage(prfx, cmd, prms, trail);
                break;
            case "NICK":
                msg = new NickMessage(prfx, cmd, prms, trail);
                break;
            case "USER":
                msg = new UserMessage(prfx, cmd, prms, trail);
                break;
            case "TOPIC":
                msg = new TopicMessage(prfx, cmd, prms, trail);
                break;
            case "JOIN":
                msg = new JoinMessage(prfx, cmd, prms, trail);
                break;
            case "PART":
                msg = new PartMessage(prfx, cmd, prms, trail);
                break;
            case "PRIVMSG":
                msg = new PrivmsgMessage(prfx, cmd, prms, trail);
                break;
            default:
                msg = new TestMessage(prfx, cmd, prms, trail);
        }

        return msg;
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

    public List<String> generateResponse(ResponseCode rc, UserInfo user){
        return generateResponse(rc, user, null);
    }

    // Create responses based on the code and the user info
    // Since this an instance method, we still have access to this message's
    // information such as prefix, command, etc.
    public List<String> generateResponse(ResponseCode rc, UserInfo user,
            List<String> other) {
        ArrayList<String> responses = new ArrayList<String>();
        switch(rc){
            case ERR_ALREADYREGISTERED:
                    responses.add(":Unauthorized command (already registered)");
                    break;
            case ERR_NEEDMOREPARAMS:
                    responses.add(this.command + " :Not enough parameters");
                    break;
            case ERR_NONICKNAMEGIVEN:
                    responses.add(":No nickname given");
                    break;
            case ERR_NICKNAMEINUSE:
                    responses.add(other.get(0)
                            + " :Nickname is already in use");
                    break;
            case ERR_NOSUCHNICK:
                    responses.add(other.get(0)
                            + ":No such nick");
                    break;
            case ERR_NOSUCHCHANNEL:
                    responses.add(other.get(0)
                            + ":No such channel");
                    break;
            case ERR_NOTREGISTERED:
                    responses.add(":You have not registered");
                    break;
            case RPL_TRYAGAIN:
                    responses.add(":We dropped the ball when"
                            + other.get(0));
                    break;
            default: break;
        }
        return responses;
    }

    public abstract List<String> executeCommand(Server server, UserInfo user);
}
