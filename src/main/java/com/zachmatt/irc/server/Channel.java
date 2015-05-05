package com.zachmatt.irc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class Channel {

    private String name;
    private HashSet<UserInfo> users;
    private String topic;

    public Channel(String name) {
        this.name = name;
        this.users = new HashSet<UserInfo>();
        this.topic = "";
    }

    public void addUser(UserInfo user) {
        users.add(user);
    }

    public HashSet<UserInfo> getUsers() {
        return users;
    }

    public boolean hasUser(UserInfo user) {
        return users.contains(user);
    }

    public void removeUser(UserInfo user) {
        users.remove(user);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
