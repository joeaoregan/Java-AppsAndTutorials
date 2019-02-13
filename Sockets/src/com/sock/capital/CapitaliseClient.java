/*
 * http://cs.lmu.edu/~ray/notes/javanetexamples/
 * 30/01/2019
 */
package com.sock.capital;

import java.io.BufferedReader;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CapitaliseClient {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter the IP address of a machine running the capitalize server:");
        String serverAddress = scanner.nextLine();
        Socket socket = new Socket(serverAddress, 9898);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        System.out.println("Enter a string to send to the server:");
        String message = scanner.nextLine();
        out.println(message);
        System.out.println(in.readLine());
        
        scanner.close();
        socket.close();
    }
}
