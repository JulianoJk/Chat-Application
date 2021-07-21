package CMD_Version;

import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CMDClient {


    public static void main(String[] args) {
        setUpNetwork();
    }

    private static void setUpNetwork(){
        try {
            Socket socket = new Socket("127.0.0.1", 5000);

            // Setup the input(receive messages)
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Setup the output(send messages
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            //Due to this is a static method, call the class method and create an method object
            new CMDClient().startListening(in);

            //When the user connects, the first input, will be the username, so print the name then start the loop.
            //If this print line is inside the loop, it will display everytime the user text, the name in front of it
            System.out.print("Type your username here: ");
            //Endless loop for the user to send messages.(Do not stop at the first message that is send)
            while(true) {
                Scanner input = new Scanner(System.in);

                String msg = input.nextLine();
                out.println(msg);
                // Now clear the stream
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startListening(BufferedReader in){
        //Pass the message listener to the thread, in order to continuously listen to messages
        Thread thread = new Thread(new MessageListener(in));
        thread.start();
    }

    private class MessageListener implements Runnable{
        private BufferedReader in;

        public MessageListener(BufferedReader in){
            this.in = in;
        }

        @Override
        public void run() {
            String message;

            try {
                while((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}