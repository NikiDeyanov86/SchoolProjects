package org.elsys.ip.sockets;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientUsername;

    public ClientHandler(Socket s) {
        try {
            this.socket = s;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientHandlers.add(this);
        } catch(IOException e ) {
            this.serverDisconnect();
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()) {
            try{
                messageFromClient = in.readLine();
                if(messageFromClient.equals("quit") || messageFromClient.equals("exit")) {
                    shutDown();
                }
            } catch(IOException e ) {
                this.serverDisconnect();
                break;
            }
        }
    }
    public static void shutDown() {
        for(ClientHandler it : clientHandlers) {
            try {
                it.socket.close();
                it.serverDisconnect();
            } catch( IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void serverDisconnect() {
        clientHandlers.remove(this);
        //System.err.println("server disconnect");
        System.exit(0);
    }

}
