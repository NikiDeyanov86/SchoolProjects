package org.elsys.ip.sockets;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    public ClientHandler(Socket s) {
        try {
            this.socket = s;
            //this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientHandlers.add(this);
        } catch(IOException e ) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()) {
            try{
                messageFromClient = in.readLine();
                if(messageFromClient.equals("quit") || messageFromClient.equals("exit")) {
                    out.println("shutdown");
                    clientHandlers.remove(this);
                    break;
                }
            } catch(IOException e ) {
                e.printStackTrace();
                System.err.println("here");
                System.exit(-1);
                break;
            }
        }
    }


}
