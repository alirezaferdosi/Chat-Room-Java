package com.ServerController;

import com.Datareview.DataCheck;
import com.Datareview.PatternCheck;
import com.Transaction.Form;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.regex.*;

public class Interaction {

    private Pattern pattern = Pattern.compile("^([rR][eE][gG][iI][sS][tT][rR][aA][iI][oO][nN]|[lL][oO][gG][iI][nN])\\s+(\\S+)\\s+(\\S+)$");
    private Matcher matcher;
    private Socket client;
    private Server.ClientHandler ch= new Server.ClientHandler(client);
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

    public void begin(BufferedReader input, BufferedWriter output) throws IOException {
        String begin ;
        String mode;
        try{
            sendMessage(output,"login or Registration :");

            while(flag){
                begin = input.readLine();
                matcher = pattern.matcher(begin);

                if(matcher.find(0)){

                    ch.username = matcher.group(2);
                    ch.password = matcher.group(3);
                    mode = matcher.group(1).toLowerCase(Locale.ENGLISH);

                    switch (mode){

                        case "login":{
                            if(patternCheck.UsernameCheck(ch.username)){
                                if(patternCheck.PasswrodCheck(ch.password)){
                                    if(dataCheck.UserExist(ch.username)){
                                        if(form.Login(ch.username,ch.password)){
                                            ch.login = true;
                                            flag = false;
                                            sendMessage(output,"You entered");
                                            System.out.println(ch.username + " entered");
                                        }else{
                                            System.out.println();
                                            sendMessage(output,"password is incorrect");
                                        }
                                    }else {
                                        System.out.println("username not found");
                                        sendMessage(output,"username not found");
                                    }
                                }else{
                                    System.out.println("password is invalid");
                                    sendMessage(output,"password is invalid");
                                }
                            }else{
                                System.out.println("username is invalid");
                                sendMessage(output,"username is invalid");
                            }
                        }
                        break;

                        case "registration":{

                            if(patternCheck.UsernameCheck(ch.username)){
                                if(patternCheck.PasswrodCheck(ch.password)){
                                    if(!dataCheck.UserExist(ch.username)){
                                        if(form.Register(ch.username,ch.password)){
                                            ch.login = false;
                                            sendMessage(output,"You Registered");
                                            ch.removeClient();
                                            ch.shutdown();
                                        }
                                    }else {
                                        sendMessage(output,"username exist");
                                    }
                                }else{
                                    System.out.println("password is invalid");
                                    sendMessage(output,"password is invalid");
                                }
                            }else{
                                System.out.println("username is invalid");
                                sendMessage(output,"username is invalid");
                            }
                        }
                        break;

                        default:{
                            System.out.println("default : Input is invalid");
                            sendMessage(output,"default : Input is invalid");
                            flag = true;
                                }
                        break;
                    }
                }else{
                    System.out.println("Input is invalid");
                    sendMessage(output,"Input is invalid");
                }
            }

        }catch(IOException e){
            ch.shutdown();
        }
    }

    public void Menu(){
        if(ch.login){

        }
    }


    private void sendMessage(BufferedWriter out, String str){
        try {
            out.write(str);
            out.newLine();
            out.flush();
        }catch(IOException e){
            System.out.println("client socket closed .");
        }

    }

}
