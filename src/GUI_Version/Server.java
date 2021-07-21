package GUI_Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class Server {
    private ServerSocket serverSocket;
    private int port;
    //Hold all the client into a list. When the server reads and print a message, do it to all the clients
    private Vector<Client> clientsList;

    //If a new user is connected, display it to the users
    private Boolean newUser = false;

    public Server(){
        try {
            clientsList = new Vector<>();
            port = 5000;
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){

        //Print a message when the server starts
        System.out.println("Starting server at port: " + port);
        // Endless loop to accept socket requests from the clients
        while (true){
            try {
                //Save the socket type to a socket variable
                Socket socket = serverSocket.accept();
                //Change the boolean type to true then a user connects
                newUser = true;

                //print message to the server when a connection is made
                System.out.println("New connection from " + socket );

                //We need to read the user's username
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Read the name of the user. This read till the line ends.
                String name = in.readLine();


                System.out.println(name + " just connected!");


                //send a message from the server
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);



                //print welcome and the name of the user. Skip a line to keep cleaner
                out.println("Welcome " + name + "\n");


                //Start new thread to read from the current client and initialize the client obj
                Client client = new Client(socket);
                //Set the client's name from the name we got
                client.setName(name);
                //add the new client to the list
                clientsList.add(client);
                ChatServerThread thread = new ChatServerThread(client, in);
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //For each client we will start a thread
    private class ChatServerThread extends Thread{
        //Clients in(bufferReader). Each thread will have its own in(reader)
        private BufferedReader in;
        //In order to get client's info(name mostly)
        private Client client;

        //Pass the bufferReader from the server(for the user)
        public ChatServerThread(Client client, BufferedReader in) {
            this.in = in;
            this.client = client;

        }

        //Here we give the input(reader) from a client and this will start reading for the client
        //Read without stopping the messages from others
        @Override
        public void run(){
            //endless loop to read the messages
            while (newUser){
                //Messages received from the clients
                String message;
                try {
                    //If a new client connects, display it to the chat screen for everyone to see it
                    for(Client newClient :clientsList) {
                        if (newUser) {
                            PrintWriter writer = new PrintWriter(newClient.getSocket().getOutputStream(), true);
                            writer.println(client.getName() +  " just connected to the chat!!");
                        }
                    }
                    while ((message= in.readLine())!=null) {
                        try {

                            //display the clients name and the message here
                            System.out.println(client.getName() + ": " + message);
                            for (Client c : clientsList) {
                                PrintWriter writer = new PrintWriter(c.getSocket().getOutputStream(), true);
                                //Display the name and the message that the person sent. At the end, skip a line, make it cleaner
                                writer.println(client.getName() + ": " + message + "\n");
                            }
                        } catch (IOException e){
                            System.out.println("REMOVE THE CLIENT!!!");

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO:Remove the client from the list
                    //If the client exits, change the boolean to false, in order to stop the loop from trying to read
                    //from that user
                    newUser = false;
                    System.out.println("No client");
                }
            }
        }


    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}