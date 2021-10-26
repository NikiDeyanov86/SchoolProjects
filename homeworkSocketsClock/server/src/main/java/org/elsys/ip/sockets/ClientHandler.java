package org.elsys.ip.sockets;


import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;
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
                else if(messageFromClient.startsWith("time")) {
                    String[] split = messageFromClient.split(" ");
                    if(split.length == 1) {
                        // time
                        LocalTime local = LocalTime.now(ZoneId.systemDefault().getRules().getStandardOffset(Instant.now()));
                        out.println(local.format(DateTimeFormatter.ofPattern("HH:mm")));
                    }
                    else if(split.length == 2) {
                        // time X
                        String[] check = split[1].split(":", 2);
                        if(!check[1].equals("00")) {
                            out.println("invalid time zone");
                        } else {
                            try {
                                LocalTime local = LocalTime.now(ZoneOffset.of(split[1]));
                                out.println(local.format(DateTimeFormatter.ofPattern("HH:mm")));
                            } catch (DateTimeException e) {
                                out.println("invalid input");
                            }
                        }
                    }
                    else {
                        out.println("invalid input");
                    }
                }
                else {
                    out.println("invalid input");
                }
            } catch(IOException e ) {
                out.println("shutdown");
                clientHandlers.remove(this);
                break;
            }
        }
    }


}
