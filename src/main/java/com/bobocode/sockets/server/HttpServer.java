package com.bobocode.sockets.server;

import com.bobocode.sockets.utils.HttpRequestUtils;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private static final int THREADS = 50;

    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    @SneakyThrows
    public HttpServer(int port) {
        serverSocket = new ServerSocket(port);
        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    public void start() {
        System.out.println("Starting http server...");
        while (true) {
            try {
                final Socket clientSocket = serverSocket.accept();
                pool.execute(() -> HttpRequestUtils.processRequest(clientSocket));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
