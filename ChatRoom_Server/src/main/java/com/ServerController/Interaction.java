package com.ServerController;

import com.Datareview.DataCheck;
import com.Datareview.PatternCheck;
import com.Transaction.Form;
import org.json.simple.JSONObject;
import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.regex.*;

public class Interaction implements Serializable {

    private Pattern pattern = Pattern.compile("^([rR][eE][gG][iI][sS][tT][rR][aA][iI][oO][nN]|[lL][oO][gG][iI][nN])\\s+(\\S+)\\s+(\\S+)$");
    private Matcher matcher;
    private Socket client;
    private Server.ClientHandler ch = new Server.ClientHandler(client);
    private Form form;
    private PatternCheck patternCheck;
    private DataCheck dataCheck;
    private Boolean flag = true;


    public Interaction(Socket client) {
        this.client = client;
        form = new Form();
        patternCheck = new PatternCheck();
        dataCheck = new DataCheck();
    }

    public void begin(ObjectInputStream input, ObjectOutputStream output) throws IOException {
        JSONObject json;
        String begin;
        String mode;
        try {
            sendMessage(output, "txt", "login or Registration :");

            while (flag) {
                json = (JSONObject) input.readObject();
                if (!json.get("header").equals("txt")) return;

                begin = (String) json.get("body");
                matcher = pattern.matcher(begin);
                if (matcher.find(0)) {

                    ch.username = matcher.group(2);
                    ch.password = matcher.group(3);
                    mode = matcher.group(1).toLowerCase(Locale.ENGLISH);

                    System.out.println(ch.username);
                    System.out.println(ch.password);
                    System.out.println(mode);

                    switch (mode) {

                        case "login": {
                            if (patternCheck.UsernameCheck(ch.username)) {
                                if (patternCheck.PasswrodCheck(ch.password)) {
                                    if (dataCheck.UserExist(ch.username)) {
                                        if (form.Login(ch.username, ch.password)) {
                                            ch.login = true;
                                            flag = false;

                                            sendMessage(output, "txt", "You entered");
                                            System.out.println(ch.username + " entered");

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

                            if (patternCheck.UsernameCheck(ch.username)) {
                                if (patternCheck.PasswrodCheck(ch.password)) {
                                    if (!dataCheck.UserExist(ch.username)) {
                                        if (form.Register(ch.username, ch.password)) {
                                            ch.login = false;
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

    public void Menu(ObjectInputStream input, ObjectOutputStream output){
            sendMessage(output,"json",form.GetGroup(ch.username));



        while (true) {

        }
    }

    public String Manual(String command) {
        String item1, item2;
        item1 = "NAME" +
                "    Group" +
                "SYNTAX" +
                "        Group [-C] [name] [information]" +
                "        Group [-AU] [username] [group_number]" +
                "        Group [-RU] [username] [group_number]" +
                "        Group [-AA] [username] [group_number]" +
                "        Group [-RA] [username] [group_number]" +
                "        Group [-E]  [group]" +
                "DESCRIPTION" +
                "        -C   ->   create group" +
                "        -AU  ->   add user" +
                "        -RU  ->   remove user" +
                "        -AA  ->   add admin" +
                "        -RM  ->   remove admin" +
                "        -E   ->   enter the group";
        item2 = "NAME" +
                "    User" +
                "SYNTAX" +
                "      Registration [username] [password]" +
                "      Login [username] [password]" +
                "      Edit  [-U] [username]" +
                "      Edit  [-B] [biography]" +
                "      Edit  [-P] [password]" +
                "      Chat  [username]" +
                "DESCRIPTION" +
                "       Registration   ->  create account" +
                "       Login    -> login to the account" +
                "       -U   ->   edit username" +
                "       -B   ->   edit biography" +
                "       -P   ->   adit password";


        switch (command.toLowerCase(Locale.ENGLISH)) {
            case "group":
                return item1;

            case "User":
                return item2;

            case "":
                return item1 + "\n" + item2;

            default:
                return "false";
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

}

