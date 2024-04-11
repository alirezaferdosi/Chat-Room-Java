package com;


import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class Client implements Runnable{
    private String Banner = "   ________          __     ____                      \n" +
            "  / ____/ /_  ____ _/ /_   / __ \\____  ____  ____ ___ \n" +
            " / /   / __ \\/ __ `/ __/  / /_/ / __ \\/ __ \\/ __ `__ \\\n" +
            "/ /___/ / / / /_/ / /_   / _, _/ /_/ / /_/ / / / / / /\n" +
            "\\____/_/ /_/\\__,_/\\__/  /_/ |_|\\____/\\____/_/ /_/ /_/     ";

    private Socket client;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean done;

    public Client(){
        this.done = false;
    }

    @Override
    public void run() {
        try{
            client = new Socket("127.0.0.1",15000);
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start();

            System.out.println(Banner);
            String inMessage;
            while ((inMessage = in.readLine()) != null){
                System.out.println(inMessage);
            }

        }catch (IOException e){
            shutdown();
        }
    }

    public void shutdown(){
        done = true;
        try {
            in.close();
            out.close();
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

    class InputHandler implements Runnable{
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            String message;

                while (!done) {
                    message = scanner.nextLine();
                    if(!message.isEmpty()){
                        sendMessage(message);
                    }
                }
        }
    }

    private void sendMessage(String message){
        try{
            out.write(message);
            out.newLine();
            out.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
