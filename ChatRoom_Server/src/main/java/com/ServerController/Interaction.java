package com.ServerController;

import com.Datareview.DataCheck;
import com.Datareview.PatternCheck;
import com.Model.Group;
import com.Model.User;
import com.Transaction.Form;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.regex.*;

import static com.Datareview.Command.*;
import static com.ServerController.Server.ClientHandler.*;
import static com.ServerController.Server.OnlineOnGroup;
import static com.ServerController.Server.get_Condition;


public class Interaction implements Serializable {
    private Matcher matcher;
    private Socket client;
    private Server.ClientHandler ch = new Server.ClientHandler(client);
    private Form form;
    private PatternCheck patternCheck;
    private DataCheck dataCheck;
    private Boolean flag = true;
    private FileController fileController;

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
                matcher = Checker(7, begin);
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
                                            login = true;
                                            flag = false;

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

        } catch (IOException e) {
            ch.shutdown();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                "      Chat  [username]\n" +
                "DESCRIPTION\n" +
                "       Registration   ->  create account\n" +
                "       Login    -> login to the account\n" +
                "       -U   ->   edit usernamen\n" +
                "       -B   ->   edit biography\n" +
                "       -P   ->   adit password\n";


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

    private void sendMessage(ObjectOutputStream out, String header, Object body) {
        try {
            out.writeObject(StreamConfig(header, body));
            out.flush();
        } catch (IOException e) {
            ch.removeClient();
            ch.shutdown();
            System.out.println("client socket closed .");
        }

    }

    private JSONObject StreamConfig(String header, Object body) {

        JSONObject json = new JSONObject();
        json.put("header", header);
        json.put("body", body);
        return json;
    }

    private void Decode(String M, ObjectOutputStream output, ObjectInputStream input) {

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
            sendMessage(output, "txt", form.AddMemeber(matcher.group(3), matcher.group(4)));
        }// add Member to the group
        else if (Checker(3, M).find(0)) {//remove user from group
            matcher = Checker(3, M);
            matcher.find(0);
            sendMessage(output, "txt", form.RemoveUser(matcher.group(3), matcher.group(4)));
        }
//            else if(Checker(4,M).find(0)){
//
//            }
//            else if(Checker(5,M).find(0)){
//
//            }
        else if (Checker(6, M).find(0)) {
            matcher = Checker(6, M);
            matcher.find(0);

            if (dataCheck.GroupExist(Long.parseLong(matcher.group(3)))) {
                Long gid = Long.parseLong(matcher.group(3));
                if (!condition.getGroup().equals(gid) && form.Membership(form.FindObject(new User(), username), form.FindObject(new Group(), gid))) {
                    Group(input,output,gid);
                    sendMessage(output, "txt", fileController.ReadFromGroup(String.valueOf(gid)));
                    condition.setChat(String.valueOf(gid));
                } else {
                    sendMessage(output, "txt", "You can't enter");
                }

            } else {
                sendMessage(output, "txt", "group does not exist");
            }
        }// enter the group
        else if (Checker(12, M).find(0)) {
            matcher = Checker(12, M);
            matcher.find(0);
            sendMessage(output, "txt", Manual(""));
        }//give the manual
        else if (Checker(13, M).find(0)) {
            matcher = Checker(13, M);
            matcher.find(0);
            sendMessage(output, "txt", Manual(matcher.group(2)));
        }//give the specific manual
        else if (Checker(14, M).find(0)) {
            matcher = Checker(14, M);
            matcher.find(0);
            sendMessage(output, "json", form.GetGroup(username));
        }// get list of group
        else {
            if (condition.getHome().equals("home")) {
                sendMessage(output, "txt", "command " + M + " does not exist ");
            }
        }
    }

    private void Group(ObjectInputStream input, ObjectOutputStream output, Long group){
        form.OnlineOnGroup(username,group);

        JSONObject json;
        try {
            sendMessage(output,"txt","welcome to our group " + username);
            while ((json = (JSONObject) input.readObject()) != null) {

                if (json.get("header").equals("json")) {}

                if(Checker(15, (String) json.get("body")).find(0)){
                    condition.setHome("home");
                    System.out.println(OnlineOnGroup.get(group));
                    OnlineOnGroup.get(group).remove(username);
                    System.out.println(OnlineOnGroup.get(group));
                    break;
                }
                else broadcast(output,input,username + " >>>" +(String) json.get("body"),group);

            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void broadcast(ObjectOutputStream output, ObjectInputStream input, String message, Long group){
        for (Object o1: get_Condition()) {
            Server.ClientHandler client = (Server.ClientHandler) o1;
            for(String o2: OnlineOnGroup.get(group)) {
                if (client.username.equals(username) && o2.equals(client.username)) {
                    sendMessage(output,"txt",message);
                }
            }
        }
    }

}


