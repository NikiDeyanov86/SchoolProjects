package org.elsys.ip.sockets;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private BufferedWriter out;
    private BufferedReader in;

    public Client(Socket socket) {
        try {
            this.clientSocket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromChat;

                while(clientSocket.isConnected()) {
                    try {
                        msgFromChat = in.readLine();
                        if(msgFromChat.equals("shutdown")) {
                            System.out.println("server disconnect");
                            System.exit(0);
                        }
                        System.out.println(msgFromChat);
                    } catch(IOException e ) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    public void sendMessage() throws IOException {
        /*out.write("");
        out.newLine();
        out.flush();*/

        Scanner scanner = new Scanner(System.in);
        while(clientSocket.isConnected()) {
            String msg = scanner.nextLine().toLowerCase();
            out.write(msg);
            out.newLine();
            out.flush();
        }
    }



    public static void main(String[] args) {
        if(args.length == 1) {
            String[] hostIp = args[0].split(":", 2);
            if(hostIp[1].length() != 4 || Integer.parseInt(hostIp[1]) <= 0) {
                System.err.println("invalid arguments");
                System.exit(1);
            }
            try {
                Socket socket = new Socket(hostIp[0], Integer.parseInt(hostIp[1]));
                Client client = new Client(socket);
                client.listen();
                client.sendMessage();
            } catch(UnknownHostException e) {
                System.err.println("invalid host");
                System.exit(3);
            } catch(IOException e) {
                System.err.println("connection not possible");
                System.exit(4);
            }

        }
        else {
            System.err.println("invalid arguments");
            System.exit(1);
        }
    }
}
