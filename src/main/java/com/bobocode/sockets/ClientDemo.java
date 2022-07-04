package com.bobocode.sockets;

import com.bobocode.sockets.client.Client;

public class ClientDemo {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 8080);
        client.start();
    }
}
