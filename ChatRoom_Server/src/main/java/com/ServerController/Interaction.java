package com.ServerController;

import com.Datareview.Command;
import com.Datareview.DataCheck;
import com.Datareview.PatternCheck;
import com.Transaction.Form;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.regex.*;

import static com.Datareview.Command.*;
import static com.ServerController.Server.*;
import static com.ServerController.Server.ClientHandler.*;


public class Interaction implements Serializable {
    private Matcher matcher;
    private Socket client;
    private Server.ClientHandler ch = new Server.ClientHandler(client);
    private Form form;
    private PatternCheck patternCheck;
    private DataCheck dataCheck;
    private Boolean flag = true;
    private FileController fileController;
    private String localusername;

    public Interaction(Socket client) {
        this.client = client;
        form = new Form();
        patternCheck = new PatternCheck();
        dataCheck = new DataCheck();
        fileController = new FileController();
    }

    public void Begin(ObjectInputStream input, ObjectOutputStream output) throws IOException {
        JSONObject json;
        String begin;
        String mode;
        try {
            sendMessage(output, "txt", "login or Registration :");

            while (flag) {
                json = (JSONObject) input.readObject();
                if (!json.get("header").equals("txt")) return;

                begin = (String) json.get("body");
                matcher = Checker(16, begin);
                if (matcher.find(0)) {

                    username = matcher.group(2);
                    password = matcher.group(3);
                    mode = matcher.group(1).toLowerCase(Locale.ENGLISH);

                    switch (mode) {

                        case "login": {
                            if (patternCheck.UsernameCheck(username)) {
                                if (patternCheck.PasswrodCheck(password)) {
                                    if (dataCheck.UserExist(username)) {
                                        if (form.Login(username, password)) {
                                            if(getClient(username) != null) {
                                                sendMessage(getClient(username).objectOutputStream,"txt","You are kicked out");
                                                getClient(username).shutdown();
                                                getClient(username).removeClient();
                                            }
                                            login = true;
                                            flag = false;
                                            localusername = username;
                                            conditionMap.put(ch.getthisClient(),new Condition());
                                            conditionMap.get(ch.getthisClient()).setHome("home");
                                            connection.put(ch.getthisClient(),username);
                                            sendMessage(output, "txt", "You entered");
                                            System.out.println(username + " entered");

                                        } else {
                                            System.out.println();
                                            sendMessage(output, "txt", "password is incorrect");
                                        }
                                    } else {
                                        System.out.println("username not found");
                                        sendMessage(output, "txt", "username not found");
                                    }
                                } else {
                                    System.out.println("password is invalid");
                                    sendMessage(output, "txt", "password is invalid");
                                }
                            } else {
                                System.out.println("username is invalid");
                                sendMessage(output, "txt", "username is invalid");
                            }
                        }
                        break;

                        case "registration": {

                            if (patternCheck.UsernameCheck(username)) {
                                if (patternCheck.PasswrodCheck(password)) {
                                    if (!dataCheck.UserExist(username)) {
                                        if (form.Register(username, password)) {
                                            login = false;
                                            sendMessage(output, "txt", "You Registered");
                                            ch.removeClient();
                                            ch.shutdown();
                                        }
                                    } else {
                                        sendMessage(output, "txt", "username exist");
                                    }
                                } else {
                                    System.out.println("password is invalid");
                                    sendMessage(output, "txt", "password is invalid");
                                }
                            } else {
                                System.out.println("username is invalid");
                                sendMessage(output, "txt", "username is invalid");
                            }
                        }
                        break;

                        default: {
                            System.out.println("default : Input is invalid");
                            sendMessage(output, "txt", "default : Input is invalid");
                            flag = true;
                        }
                        break;
                    }
                } else {
                    System.out.println("Input is invalid");
                    sendMessage(output, "txt", "Input is invalid");
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            ch.shutdown();
        }
    }

    public void Home(ObjectInputStream input, ObjectOutputStream output) {
        try {
            JSONObject jsonObject;
            String body;

            while ((jsonObject = (JSONObject) input.readObject()) != null) {
                if (!jsonObject.get("header").equals("txt")) continue;

                body = (String) jsonObject.get("body");
                Decode(body, output, input);

            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("input not found");
            e.printStackTrace();
        }

    }

    private void Decode(String M, ObjectOutputStream output, ObjectInputStream input) {
        String name = localusername;
        if (Checker(0, M).find(0)) {
            matcher = Checker(0, M);
            matcher.find(0);
            form.CreateGroup(matcher.group(3), username, matcher.group(4));
            sendMessage(output, "txt", "group created");
        }// create the group
        else if (Checker(1, M).find(0)) {
            matcher = Checker(1, M);
            matcher.find(0);
            form.CreateGroup(matcher.group(3), username);
            sendMessage(output, "txt", "group created");
        }// create the group
        else if (Checker(2, M).find(0)) {
            matcher = Checker(2, M);
            matcher.find(0);
            sendMessage(output, "txt", form.AddMemeber(matcher.group(3), Long.valueOf(matcher.group(4)), localusername));
        }// add Member to the group
        else if (Checker(3, M).find(0)) {
            matcher = Checker(3, M);
            matcher.find(0);
            sendMessage(output, "txt", form.RemoveUser(matcher.group(3), Long.parseLong(matcher.group(4)),localusername));
        }// remove user from group
        else if (Checker(6, M).find(0)) {
            matcher = Checker(6, M);
            matcher.find(0);

            if (dataCheck.GroupExist(Long.parseLong(matcher.group(3)))) {
                Long gid = Long.parseLong(matcher.group(3));

                if (conditionMap.get(getClient(name)).getGroup() != gid && form.Membership(name,gid)) {
                    Group(input,output,gid);
                } else {
                    sendMessage(output, "txt", "You can't enter");
                }

            } else {
                sendMessage(output, "txt", "group does not exist");
            }

        }// enter to the group
        else if (Checker(8, M).find(0)) {
            matcher = Checker(8, M);
            matcher.find(0);
            sendMessage(output, "json", form.GetGroup(username));

        }// get list of group
        else if (Checker(12, M).find(0)){
            matcher = Checker(12,M);
            matcher.find(0);

            if(dataCheck.UserExist(matcher.group(3))){
                Chat(input,output, matcher.group(3));
            }else {
                sendMessage(output,"txt","User not found");
            }
        }// enter to the chat
        else if (Checker(14, M).find(0)) {
            matcher = Checker(14, M);
            matcher.find(0);
            sendMessage(output, "txt", Manual(""));

        }// give the manual
        else if (Checker(15, M).find(0)) {
            matcher = Checker(15, M);
            matcher.find(0);
            sendMessage(output, "txt", Manual(matcher.group(2)));

        }// give the specific manual
        else if (Checker(17, M).find(0)){
            sendMessage(getClient(localusername).objectOutputStream,"txt","you log out");
            getClient(localusername).shutdown();
            getClient(localusername).removeClient();
        }// Log out from application
        else {
            if (conditionMap.get(getClient(name)).getHome().equals("home")) {
                sendMessage(output, "txt", "command " + M + " does not exist ");
            }
        }
    }

    private void Group(ObjectInputStream input, ObjectOutputStream output, Long group){
        form.OnlineOnGroup(localusername,group);

        sendMessage(output,"txt","welcome to our group " + localusername);
        sendMessage(output, "txt", fileController.ReadFromGroup(String.valueOf(group)));
        conditionMap.get(getClient(localusername)).setGroup(group);

        JSONObject json;
        try {
            while ((json = (JSONObject) input.readObject()) != null) {

                if (json.get("header").equals("json")) {}

                if(Checker(7, (String) json.get("body")).find(0)){
                    sendMessage(output,"txt","you exit");
                    conditionMap.get(getClient(localusername)).setHome("home");
                    OnlineOnGroup.get(group).remove(localusername);
                    break;
                }
                else{
                    broadcast(localusername + ": " + json.get("body"),group);
                    fileController.WriteToGroup(localusername + ": " + json.get("body"), String.valueOf(group));
                }

            }
        } catch(IOException | ClassNotFoundException e){
            System.err.println("error from client");
            e.printStackTrace();
        }
    }

    private void Chat(ObjectInputStream input,ObjectOutputStream output, String username){
        conditionMap.get(getClient(localusername)).setChat(username);
        sendMessage(output,"txt",fileController.ReadfromChat(localusername,username));
        JSONObject json;
        try{
            while((json = (JSONObject) input.readObject()) != null){
                if(!json.get("header").equals("json")){}
                if(Checker(13, (String) json.get("body")).find(0)){
                    sendMessage(output,"txt","you exit");
                    conditionMap.get(getClient(localusername)).setHome("home");
                    break;
                }
                else{
                    if(getClient(username) != null && conditionMap.get(getClient(username)).getChat().equals(localusername)){
                        sendMessage(getClient(username).objectOutputStream,"txt",(String)json.get("body"));
                        fileController.WriteToChat((String) json.get("body"),localusername,username);
                    }
                    else {
                        fileController.WriteToChat((String) json.get("body"),localusername,username);
                    }
                }

            }
        }catch(IOException | ClassNotFoundException e){
            System.err.println("error from client");
            e.printStackTrace();
        }
    }

    private void broadcast(String message, Long group){
        for(String o1: OnlineOnGroup.get(group)) {
            for(Object o2: get_Connection().keySet()){
                Server.ClientHandler client = (Server.ClientHandler) o2;
                if (!get_Connection().get(client).equals(localusername) && o1.equals(get_Connection().get(client))) {
                    sendMessage(client.objectOutputStream,"txt",message);
                    break;
                }
            }

        }
    }

    private void sendMessage(ObjectOutputStream out, String header, Serializable body) {
        try {
            out.writeObject(StreamConfig(header, body));
            out.flush();
        } catch (IOException e) {
            ch.removeClient();
            ch.shutdown();
            System.out.println("client socket closed .");
        }

    }

    private JSONObject StreamConfig(String header, Serializable body) {

        JSONObject json = new JSONObject();
        json.put("header", header);
        json.put("body", body);
        return json;
    }

    private ClientHandler getClient(String username){
        for(ClientHandler o:connection.keySet()){
            if((connection.get(o).equals(username))) return o;
        }
        return null;
    }

    public String Manual(String command) {
        String item1, item2;
        item1 = "NAME\n" +
                "    Group\n" +
                "SYNTAX\n" +
                "        Group [-C] [name] [information]\n" +
                "        Group [-AU] [username] [group_number]\n" +
                "        Group [-RU] [username] [group_number]\n" +
                "        Group [-AA] [username] [group_number]\n" +
                "        Group [-RA] [username] [group_number]\n" +
                "        Group [-EN]  [group_number]\n" +
                "        Group [-EX]  [group_number]\n" +
                "        Group [-L]\n" +
                "DESCRIPTION\n" +
                "        -C   ->   create group\n" +
                "        -AU  ->   add user\n" +
                "        -RU  ->   remove user\n" +
                "        -AA  ->   add admin\n" +
                "        -RM  ->   remove admin\n" +
                "        -EN  ->   enter the group\n" +
                "        -Ex  ->   left the group\n" +
                "        -RM  ->   remove admin\n" +
                "        -L   ->   List of groups\n";
        item2 = "NAME\n" +
                "    User\n" +
                "SYNTAX\n" +
                "      Registration [username] [password]\n" +
                "      Login [username] [password]\n" +
                "      Edit  [-U] [username]\n" +
                "      Edit  [-B] [biography]\n" +
                "      Edit  [-P] [password]\n" +
                "      Chat  [-EN] [username]\n" +
                "      Chat  [-EX]\n" +
                "DESCRIPTION\n" +
                "       Registration   ->  create account\n" +
                "       Login    -> login to the account\n" +
                "       -U   ->   edit usernamen\n" +
                "       -B   ->   edit biography\n" +
                "       -P   ->   adit password\n" +
                "       -Ex ->   Exit form Chat\n";

        switch (command.toLowerCase(Locale.ENGLISH)) {
            case "group":
                return item1;

            case "user":
                return item2;

            case "":
                return item1 + "\n" + item2;

            default:
                return "No document was found for the command " + command;
        }
    }

}


