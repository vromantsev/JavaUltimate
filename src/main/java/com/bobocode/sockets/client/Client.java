package com.bobocode.sockets.client;

import lombok.Cleanup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class Client {

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = Objects.requireNonNull(host);
        this.port = Objects.requireNonNull(port);
    }

    public void start() {
        try {
            @Cleanup var socket = new Socket(this.host, this.port);
            @Cleanup var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            @Cleanup var out = new PrintWriter(socket.getOutputStream(), true);
            out.println("""
                    GET /hello?name=Vlad HTTP/1.1
                    Host: 127.0.0.1
                    Accept: */*
                                        
                    """);
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
