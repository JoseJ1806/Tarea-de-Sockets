package p1;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;
/**
 * 
 * 
 * Used to get input from the server
 * 
 */
public class TaskReadThread implements Runnable {
	    //private variables
	    Socket socket;
	    SocketClient client;
	    DataInputStream input;
	    
	    //constructor
	    public TaskReadThread(Socket socket, SocketClient client) {
	        this.socket = socket;
	        this.client = client;
	    }

	    @Override 
	    public void run() {
	        //continuous loop 
	        while (true) {
	            try {
	                //Create data input stream
	                input = new DataInputStream(socket.getInputStream());

	                //get input from the client
	                String message = input.readUTF();

	                //append message in the TextArea
	                Platform.runLater(() -> {
	                    //display the message in the TextArea
	                    client.messages.appendText(message + "\n");
	                });
	            } catch (IOException ex) {
	                System.out.println("Error reading from server: " + ex.getMessage());
	                ex.printStackTrace();
	                break;
	            }
	        }
	    }
	}

