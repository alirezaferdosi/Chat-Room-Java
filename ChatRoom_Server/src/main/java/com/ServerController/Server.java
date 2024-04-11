package com.ServerController;

import org.eclipse.persistence.internal.jaxb.json.schema.model.JsonType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private static Vector<ClientHandler> connection;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private boolean done;

    private final Integer PORT;

    public Server(Integer PORT){
        this.PORT = PORT;
        done = false;
        connection = new Vector<>();
    }

    @Override
    public void run(){
        try{
            serverSocket = new ServerSocket(PORT);
            pool = Executors.newCachedThreadPool();

            while(!done){
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                connection.add(handler);
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

            for(ClientHandler ch: connection){
                System.out.println("server shutdown");
                ch.shutdown();
                System.exit(1);
            }
        }catch(IOException e){
            System.out.println("client disconnect 1");
        }
    }

    public static class ClientHandler implements Runnable {

        private final Socket client;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;
        public Boolean login;
        public String username;
        public String password;

        public  ClientHandler(Socket client){
            this.client = client;
            login = false;
        }

        @Override
        public void run(){
            try {
                Interaction intraction = new Interaction(client);
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                bufferedWriter = new BufferedWriter((new OutputStreamWriter(client.getOutputStream())));

                intraction.begin(bufferedReader, bufferedWriter);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void removeClient(){
            connection.remove(this);
        }

        public void shutdown(){
            try{
                bufferedReader.close();
                bufferedWriter.close();
                if(client.isConnected()){
                    System.out.println("inputhandler shutdown");
                    client.close();
                }
            }catch(IOException e){
                System.out.println("client disconnect 2");
//                e.printStackTrace();
            }catch (NullPointerException r){
                System.out.println("value not found");
            }

        }
    }
}
