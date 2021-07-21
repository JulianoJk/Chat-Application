package CMD_Version;

import java.net.Socket;

// Saves which socket is and the name of the client
public class Client {
    private Socket socket;
    //Name of the client
    private String name;

    public Client(Socket socket){
        this.socket = socket;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }
}