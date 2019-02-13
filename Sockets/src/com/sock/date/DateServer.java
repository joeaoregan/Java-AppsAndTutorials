/*
 * http://cs.lmu.edu/~ray/notes/javanetexamples/
 * 30/01/2019
 */
package com.sock.date;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * A TCP server that runs on port 9090. When a client connects, it sends the client the
 * current date and time, then closes the connection with that client. This is arguably
 * just about the simplest server you can write. While simple, it has the disadvantage
 * that a client has to be completely served its date before the server will be able to
 * handle another client.
 */
public class DateServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket listener = new ServerSocket(9090)) {
            System.out.println("The date server is running");
            while (true) {
                try (Socket socket = listener.accept()) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString());
                }
            }
        }
    }
}