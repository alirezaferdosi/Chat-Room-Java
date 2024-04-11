package com;

import com.ServerController.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        new Server(15000).run();
    }
}
