package com.bobocode.nasa.utils;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;

public final class SocketUtils {

    private SocketUtils() {}

    /**
     * Allows to open a client socket. Has useSsl option which allows to open secure socket connection.
     *
     * @param host host
     * @param port port
     * @param useSsl configuration option. If true, then secure socket connection is opened, otherwise unsecure connection
     *               if opened
     * @return client socket
     */
    public static Socket createSocket(final String host, final int port, final boolean useSsl) throws IOException {
        if (useSsl) {
            return SSLSocketFactory.getDefault().createSocket(host, port);
        } else {
            return new Socket(host, port);
        }
    }
}
