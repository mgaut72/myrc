package com.zachmatt.irc.messages;

public enum ResponseCode {
    // Replies
    // User welcome
    RPL_WELCOME("001"),
    RPL_YOURHOST("002"),
    RPL_CREATED("003"),
    RPL_MYINFO("004"),

    // Away reply
    RPL_AWAY("301"),

    // WHOIS
    RPL_WHOISUSER("311"),
    RPL_WHOISSERVER("312"),
    RPL_WHOISOPERATOR("313"),
    RPL_WHOISIDLE("317"),
    RPL_ENDOFWHOIS("318"),
    RPL_WHOISCHANNELS("319"),

    // WHOWAS
    RPL_WHOWASUSER("314"),
    RPL_ENDOFWHOWAS("369"),

    // LIST
    RPL_LIST("322"),
    RPL_LISTEND("323"),

    // Channel Replies
    RPL_UNIQOPIS("325"),
    RPL_NOTOPIC("331"),
    RPL_TOPIC("332"),
    RPL_INVITING("341"),
    RPL_INVITELIST("346"),
    RPL_ENDOFINVITELIST("347"),

    // WHO
    RPL_WHOREPLY("352"),
    RPL_ENDOFWHO("315"),

    // NAMES
    RPL_NAMEREPLY("353"),
    RPL_ENDOFNAMES("366"),

    // Channel ban list
    RPL_BANLIST("367"),
    RPL_ENDOFBANLIST("368"),

    // OPER
    RPL_YOUROPER("381"),

    // In case the server drops the ball
    RPL_TRYAGAIN("263"),

    // Errors
    ERR_NOSUCHNICK("401"),
    ERR_NOSUCHCHANNEL("403"),
    ERR_CANNOTSENDTOCHAN("404"),
    ERR_WASNOSUCHNICK("406"),
    ERR_TOOMANYTARGETS("407"),
    ERR_NOORIGIN("409"),
    ERR_NORECIPIENT("411"),
    ERR_NOTEXTTOSEND("412"),
    ERR_UNKNOWNCOMMAND("421"),
    ERR_NONICKNAMEGIVEN("431"),
    ERR_ERRONEOUSNICKNAME("432"),
    ERR_NICKNAMEINUSE("433"),
    ERR_NICKCOLLISION("436"),
    ERR_USERNOTINCHANNEL("441"),
    ERR_NOTONCHANNEL("442"),
    ERR_USERONCHANNEL("443"),
    ERR_NOTREGISTERED("451"),
    ERR_NEEDMOREPARAMS("461"),
    ERR_ALREADYREGISTERED("462"),

    // Channel errors
    ERR_UNKNOWNMODE("472"),
    ERR_INVITEONLYCHAN("473"),

    ERR_NOPRIVILEGES("481"),
    ERR_CANTKILLSERVER("483"),
    ERR_NOOPERHOST("491"),
    ERR_UMODEUNKNOWNFLAG("501"),
    ERR_USERSDONTMATCH("502");

    String codeNum;
    ResponseCode(String codeNum) {
        this.codeNum = codeNum;
    }
}
