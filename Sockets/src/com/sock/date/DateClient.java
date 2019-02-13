/*
 * http://cs.lmu.edu/~ray/notes/javanetexamples/
 * http://www.technicalkeeda.com/java-tutorials/get-local-ip-address-and-hostname-in-java
 * 30/01/2019
 */
package com.sock.date;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import java.net.InetAddress;	// get ip address

/**
 * A command line client for the date server. It prompts you, at the console, to enter
 * the IP address of a server, then displays the response from the server on success,
 * otherwise it crashes and dumps the exception trace.
 */
public class DateClient {
    public static void main(String[] args) throws IOException {
    	// IP Address and Host
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());

        System.out.println("Enter the IP address of a machine running the date server:");
        Scanner sc = new Scanner(System.in);
        String serverAddress = sc.nextLine();
        Socket socket = new Socket(serverAddress, 9090);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
        System.out.println("Server response: " + response);
        
        socket.close();
        sc.close();
    }
}