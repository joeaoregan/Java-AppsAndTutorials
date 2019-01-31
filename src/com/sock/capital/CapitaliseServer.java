/*
 * http://cs.lmu.edu/~ray/notes/javanetexamples/
 * 30/01/2019
 */
package com.sock.capital;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server program which accepts requests from clients to capitalize strings. When
 * clients connect, a new thread is started to handle a client. The receiving of the
 * client data, the capitalizing, and the sending back of the data is handled on the
 * worker thread, allowing much greater throughput because more clients can be handled
 * concurrently.
 */
public class CapitaliseServer {

    /**
     * Application method to run the server listening on port 9898. When a client
     * connects, the server spawns a new thread to do the servicing and immediately
     * returns to listening. Just for fun, he server keeps a unique client id for each
     * client to show interesting logging messages.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The capitalization server is running.");
        ExecutorService pool = Executors.newFixedThreadPool(20);
        int clientNumber = 0;
        
        try (ServerSocket listener = new ServerSocket(9898)) {
            while (true) {
                pool.execute(new Capitalizer(listener.accept(), clientNumber++));
            }
        }
    }

    private static class Capitalizer implements Runnable {
        private Socket socket;
        private int clientNumber;

        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New client #" + clientNumber + " connected at " + socket);
        }

        public void run() {
            try {
            	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(in.readLine().toUpperCase());
            } catch (Exception e) {
                System.out.println("Error handling client #" + clientNumber);
            } finally {
                try { socket.close(); } catch (IOException e) {}
                System.out.println("Connection with client # " + clientNumber + " closed");
            }
        }
    }
}
