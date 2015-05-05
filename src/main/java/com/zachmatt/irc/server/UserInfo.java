package com.zachmatt.irc.server;

import java.io.ObjectOutputStream;

public class UserInfo {
    public long lastTimestampSeconds;
    public boolean isAway;
    public boolean isInvisible;
    public boolean isOperator;
    public RegistrationState registrationState;

    public String nickname;
    public String realname;

    public ObjectOutputStream outStream;

    public UserInfo(ObjectOutputStream outStream) {
        this.outStream = outStream;

        this.lastTimestampSeconds = System.currentTimeMillis() / 1000;
        this.isAway = false;
        this.isInvisible = false;
        this.isOperator = false;
        this.registrationState = RegistrationState.NONE_SENT;
    }

    public boolean isRegistered() {
        return this.registrationState == RegistrationState.REGISTERED;
    }

    public enum RegistrationState {
        NONE_SENT(0),
        PASS_SENT(1),
        NICK_SENT(2),
        REGISTERED(3);

        int value;
        RegistrationState(int value) {
            this.value = value;
        }
    }
}
