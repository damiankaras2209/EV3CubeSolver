package me.damiankaras.ev3cubesolver;

import com.jcraft.jsch.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {

    static final int TYPE_CUBE = 0;
    static final int TYPE_COLOR = 1;

    private static Network instance = new Network();

    public static Network getInstance() {
        return instance;
    }

    ServerSocket serverSocket = null;
    PrintWriter out = null;

    Network() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(1337);
                } catch(IOException e) {
                    System.err.println("Could not listen on port: 1337.");
                    System.exit(1);
                }

                System.out.println("Listening on port 1337");

                while(true) {

                    Socket clientSocket = null;

                    try {
                        clientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        System.err.println("Accept failed.");
                        continue;
                    }

                    System.out.println("Client connected");

                    BufferedReader in = null;

                    try {

                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        String inputLine, outputLine;

//                outputLine = kkp.processInput(null);
//                out.println(outputLine);

                        while ((inputLine = in.readLine()) != null) {

                            inputLine = inputLine.trim();
                            processReceived(inputLine);

//                            outputLine = Integer.toString(Integer.parseInt(inputLine) + 1);
//                            out.println(outputLine);

                        }

                    } catch (IOException e) {
//                e.printStackTrace();
                        System.out.println("Client disconnected");
                    }

                    try {
                        if(out != null) {
                            out.close();
                            out = null;
                        }
                        if(in != null) {
                            in.close();
                            in = null;
                        }
                        clientSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    void send(int type, String str) {
        if(out != null) {
            System.out.println("Sending: " + type + "/" + str);
            out.println(type + "/" + str);
        }
        else {
            System.out.println("No client to send data to");
        }
    }

    void processReceived(String str) {
        System.out.println("Received: " + str);
    }

    void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {

        }
    }
}
