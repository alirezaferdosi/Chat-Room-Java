package com.ServerController;


import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.Transaction.Form.GetAllgroups;

public class Server implements Runnable{
    public static Map<ClientHandler,String> connection;
    public static Map<Long, ArrayList<String>> OnlineOnGroup;
    public static Map<ClientHandler, Condition>  conditionMap;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private boolean done;
    private final Integer PORT;
    static ClientHandler object;

    public Server(Integer PORT){
        this.PORT = PORT;
        done = false;
        connection = new HashMap<>();
        OnlineOnGroup = new HashMap<>();
        conditionMap = new HashMap<>();
        for(Object o: GetAllgroups()){
            OnlineOnGroup.put((Long) o,new ArrayList<>());
        }
    }

    public static Map get_Connection(){
        return connection;
    }

    @Override
    public void run(){
        try{
            serverSocket = new ServerSocket(PORT);
            pool = Executors.newCachedThreadPool();

            while(!done){
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                object = handler;
                connection.put(handler,"");
                pool.execute(handler);
            }
        }catch(IOException e){
            e.printStackTrace();
            shutdown();
        }
    }

    public void shutdown(){
        try{
            done = true;
            pool.shutdown();
            if (!serverSocket.isClosed()){
                serverSocket.close();
            }

            for(ClientHandler ch: connection.keySet()){
                System.out.println("server shutdown");
                ch.shutdown();
                System.exit(1);
            }
        }catch(IOException e){
            System.out.println("client disconnect 1");
        }
    }

    public static class ClientHandler implements Runnable,Serializable {

        private final Socket client;
        public ObjectOutputStream objectOutputStream;
        public ObjectInputStream objectInputStream;
        public static Boolean login;
        public static String username;
        public static String password;

        public  ClientHandler(Socket client){
            this.client = client;
            login = false;
        }

        public ClientHandler getthisClient(){
            return object;
        }

        @Override
        public void run(){
            try {
                Interaction intraction = new Interaction(client);
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectInputStream = new ObjectInputStream(client.getInputStream());

                intraction.Begin(objectInputStream, objectOutputStream);
                connection.put(this,username);
                if(login)   intraction.Home(objectInputStream,objectOutputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void removeClient(){
            connection.remove(this);
        }

        public void shutdown(){
            try{
                objectInputStream.close();
                objectOutputStream.close();

                if(client.isConnected()){
                    System.out.println("inputhandler shutdown");
                    client.close();
                }
            }catch(IOException e){
                System.out.println("client disconnect 2");
            }catch (NullPointerException r){
                System.out.println("value not found");
            }

        }
    }
}
