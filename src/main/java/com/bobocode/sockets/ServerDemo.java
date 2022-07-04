package com.bobocode.sockets;

import com.bobocode.sockets.server.HttpServer;

public class ServerDemo {
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);
        server.start();
    }
}
