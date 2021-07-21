package GUI_Version;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class GUIClient extends JFrame {
    //Create a JButton object
    private JButton sendButton;
    // Create a textField
    private JTextField textField;

    private BufferedReader in;

    private PrintWriter out;

    private Socket frameSocket;
    ;
    //inner class to send messages and receive from the server
    private SendAction sendAction;

    //Save user's name
    private String nameinput;

    //Assign the ip and the port of the socket
    private String ip = "127.0.0.1";
    private int port = 5000;

    private AppPanel textPanel;

    public GUIClient() {

        setTitle("Messenger v0.5");
        setSize(650, 650);

        // Create a panel object
        textPanel = new AppPanel();
        // Add the panel to the center of the frame
        add(textPanel, BorderLayout.EAST);

        //Set a panel to hold the buttons
        JPanel fieldAndButton = new JPanel();

        // Create the button to sent the text
        sendButton = new JButton("Sent");

        //initialize the send action class
        sendAction = new SendAction();
        sendButton.addActionListener(sendAction);

        //Create a textField to type the messages
        textField = new JTextField();
        //Set a font for the text
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        //Add the keylistener to the textfield
        textField.addKeyListener(sendAction);


        textField.setPreferredSize(new Dimension(450, 40));


        // Add the button and text field to the panel now
        fieldAndButton.add(textField);
        fieldAndButton.add(sendButton);


        // Add the panel which holds the button and text-field in the frame, and place it to the bottom of the frame
        add(fieldAndButton, BorderLayout.SOUTH);

        getNamenput();


        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(8, 4, 72));

    }


    public String getNamenput(){
        //repeat until the user typed a username
        do {
            //TODO: Make bigger font and set bigger size
            //Popup to take user's name
            nameinput = JOptionPane.showInputDialog(GUIClient.this,
                    "What is your name?", null);
        }
        while (nameinput.length()==0 || nameinput.isBlank() || nameinput.isEmpty());
        return nameinput;
    }

    public static void main(String[] args) {
       GUIClient guiClient =  new GUIClient();
        try {
            guiClient.frameSocket = new Socket(guiClient.ip, guiClient.port);

            //send a message to the server
           guiClient.out = new PrintWriter(guiClient.frameSocket.getOutputStream(),  true);

            guiClient.out.println(guiClient.nameinput);

            //We need to read the message from the server
            guiClient.in = new BufferedReader(new InputStreamReader(guiClient.frameSocket.getInputStream()));

            String msg = guiClient.in.readLine();

            System.out.println(msg);
            guiClient.startListening(guiClient.in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startListening(BufferedReader in){
        //Pass the message listener to the thread, in order to continuously listen to messages
        Thread thread = new Thread(new MessagesListener(in));
        thread.start();
    }

    private class MessagesListener implements Runnable{
        private BufferedReader in;

        public MessagesListener(BufferedReader in){
            this.in = in;
        }

        @Override
        public void run() {
            String message;

            try {
                while((message = in.readLine()) != null) {
                    textPanel.setTextLabel(message);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendAction implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //print the messages that are send
            String messages = textField.getText();
            if(!(messages.length()==0 || messages.isBlank())) {
                textField.setText("");
                System.out.println(messages);
                //Send the message to the server
                out.println(messages);
                //Display the text received from the server to the chat app textArea
//                displayTextFromServer();

                    /*
                    After the text submitted, the in order to type again, the user needs to click inside the
                    text field again. By using a .grabFocus() and .requestFocus() methods, after the user sends the text
                    the text field is focused automatically, without having to manually click inside the text field again.
                     */
                textField.grabFocus();
                textField.requestFocus();
            }else{
                //Do nothing
            }
        }

        // Sent message with the keyboard( The ENTER physical Keyboard)
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ENTER) {
                //print the messages that are send
                String messages = textField.getText();
                if (messages.length() == 0 || messages.isBlank()) {
                    //Do nothing
                } else {
                    textField.setText("");
                    System.out.println(messages);
                    //Send the message to the server
                    out.println(messages);
                    //Display the text received from the server to the chat app textArea
//                    displayTextFromServer();
                    /*
                    After the text submitted, the in order to type again, the user needs to click inside the
                    text field again. By using a .grabFocus() and .requestFocus() methods, after the user sends the text
                    the text field is focused automatically, without having to manually click inside the text field again.
                     */
                    textField.grabFocus();
                    textField.requestFocus();
                }

            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }


        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}

