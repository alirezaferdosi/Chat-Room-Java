package com.ServerController;

import org.json.simple.JSONObject;

public class Condition {
    private Long Group;
    private String Chat;
    private String Home;

    public Condition() {
        Home = "home";
    }

    public Long getGroup() {
        return Group;
    }

    public void setGroup(Long group) {
        Group = group;
        Chat = "";
        Home = "";
    }

    public String getChat() {
        return Chat;
    }

    public void setChat(String chat) {
        Chat = chat;
        Group = Long.valueOf(0);
        Home = "";
    }

    public String getHome() {
        return Home;
    }

    public void setHome(String home) {
        Home = home;
        Group = Long.valueOf(0);
        Chat = "";
    }
}
