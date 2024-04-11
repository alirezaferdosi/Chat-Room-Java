//package com.ServerController;
//
//import java.io.*;
//import java.net.Socket;
//
//
//public class ClientHandler implements Runnable {
//
//    private final Socket client;
//    private BufferedReader bufferedReader;
//    private BufferedWriter bufferedWriter;
//    private Intraction intraction;
//    public Boolean login;
//    public String username;
//    public String password;
//
//    public  ClientHandler(Socket client){
//        this.client = client;
//        login = false;
//    }
//
//    @Override
//    public void run(){
//        try {
//            intraction = new Intraction(client);
//            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            bufferedWriter = new BufferedWriter((new OutputStreamWriter(client.getOutputStream())));
//
//            intraction.begin(bufferedReader, bufferedWriter);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void shutdown(){
//        try{
//            bufferedReader.close();
//            bufferedWriter.close();
//            if(client.isConnected()){
//                client.close();
//            }
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//
//    }
//}