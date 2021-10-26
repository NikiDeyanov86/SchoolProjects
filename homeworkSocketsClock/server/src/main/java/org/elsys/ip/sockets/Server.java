package org.elsys.ip.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket socket) {
        this.serverSocket = socket;
    }

    public void start() throws IOException {
        while(!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            //System.out.println("New client connected.");
            ClientHandler clientHandler = new ClientHandler(socket);

            Thread thread = new Thread(clientHandler);
            thread.start();
        }

        stop();

    }

    public void stop() throws IOException {
        serverSocket.close();
    }


    public static void main(String[] args) {
        if(args.length == 1 && Integer.parseInt(args[0]) > 0) {
            try {
                if(args[0].length() != 4) {
                    System.err.println("invalid arguments");
                    System.exit(1);
                }
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
                Server server = new Server(serverSocket);
                server.start();
            } catch(BindException e) {
                System.err.println("port is already in use");
                System.exit(2);
            } catch(IOException e) {
                System.err.println("port is already in use");
                System.exit(2);
            }
        }
        else {
            System.err.println("invalid arguments");
            System.exit(1);
        }
    }

}