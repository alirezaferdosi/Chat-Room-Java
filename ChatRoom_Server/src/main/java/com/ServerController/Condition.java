package com.ServerController;

import org.json.simple.JSONObject;

public class Condition {
    private JSONObject json;

    public Condition(){
        json = new JSONObject();
        json.put("group","");
        json.put("chat","");
        json.put("home","home");
    }

    public Object getHome(){
        return json.get("home");
    }

    public Object getChat(){
        return json.get("chat");
    }

    public Object getGroup(){
        return json.get("group");
    }

    public void setHome(String value){
        json.put("home", value);
        json.put("chat", "");
        json.put("group", "");

    }

    public void setGroup(String value){
        json.put("home", "");
        json.put("chat", "");
        json.put("group", value);

    }

    public void setChat(String value){
        json.put("home", "");
        json.put("chat", value);
        json.put("group", "");

    }
}
