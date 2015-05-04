package com.zachmatt.irc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class Channel {

    private String name;
    private HashSet<UserInfo> users;

    public Channel(String name) {
        this.name = name;
        this.users = new HashSet<UserInfo>();
    }

    public void addUser(UserInfo user) {
        users.add(user);
    }

    public HashSet<UserInfo> getUsers() {
        return users;
    }

}
