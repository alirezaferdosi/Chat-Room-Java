package com;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;
import org.json.simple.JSONObject;


public class Client implements Runnable{
    private String Banner = "   ________          __     ____                      \n" +
            "  / ____/ /_  ____ _/ /_   / __ \\____  ____  ____ ___ \n" +
            " / /   / __ \\/ __ `/ __/  / /_/ / __ \\/ __ \\/ __ `__ \\\n" +
            "/ /___/ / / / /_/ / /_   / _, _/ /_/ / /_/ / / / / / /\n" +
            "\\____/_/ /_/\\__,_/\\__/  /_/ |_|\\____/\\____/_/ /_/ /_/     ";

    private Socket client;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private boolean done;

    public Client(){
        this.done = false;
    }

    @Override
    public void run() {
        try{
            client = new Socket("127.0.0.1",16000);

            objectInputStream = new ObjectInputStream(client.getInputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());

            OutputHandler inputHandler = new OutputHandler();
            InputHandler inputObjecthandler = new InputHandler();

            System.out.println(Banner);

            Thread t1 = new Thread(inputHandler);
            Thread t2 = new Thread(inputObjecthandler);

            t1.start();
            t2.start();


        }catch (IOException e){
            shutdown();
        }
    }

    public void shutdown(){
        done = true;
        try {
            bufferedReader.close();
            bufferedWriter.close();
            if(!client.isClosed()){
                client.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            System.out.println("server or client done");
            System.exit(1);
        }
    }

    private class OutputHandler implements Runnable{

        @Override
        public void run() {
                Scanner scanner = new Scanner(System.in);
                String message;
                while (!done) {
                    message = scanner.nextLine();
                    if (!message.isEmpty()) {
                        if(message.toLowerCase(Locale.ENGLISH).equals("cls") || message.toLowerCase(Locale.ENGLISH).equals("clear")){
                            System.out.println("\033[H\033[2J");

                        }else{
                            sendMessage(objectOutputStream,"txt",message);

                        }
                    }
                }

        }
    }

    private class InputHandler implements Runnable{

        @Override
        public void run(){
            try {
                JSONObject jsonObject;
                while ( (jsonObject = (JSONObject) objectInputStream.readObject()) != null) {

                        if (jsonObject.get("header").equals("json")) {

                            JSONObject json = (JSONObject) jsonObject.get("body");
                            for (Object o : json.keySet())
                                System.out.println(o + " : " + json.get(o));

                        } else if (jsonObject.get("header").equals("txt")) {
                            System.out.println(jsonObject.get("body"));
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                } catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
        }
    }


    private void sendMessage(ObjectOutputStream out,String mode, Object body){
        try {
            out.writeObject(StreamConfig(mode,body));
            out.flush();
        }catch(IOException e){
            System.out.println("client socket losed .");
        }

    }

    private JSONObject StreamConfig(String header, Object body){

        JSONObject json = new JSONObject();
        json.put("header",header);
        json.put("body",body);
        return json;
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
