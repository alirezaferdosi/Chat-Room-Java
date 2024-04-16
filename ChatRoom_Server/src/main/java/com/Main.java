package com;

import com.ServerController.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Server(16000).run();
    }
}
