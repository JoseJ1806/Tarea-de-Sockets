package p1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;

/**
 * 
 * This class represents each new connection
 *
 */
public class TaskClientConnection implements Runnable {
	
	Socket socket;
	SocketServer server;
	//Create data input and output streams
	DataInputStream input;
	DataOutputStream output;
	
	public TaskClientConnection (Socket socket, SocketServer server) {
		this.socket = socket;
		this.server = server;	
	}

	@Override
	public void run() {
		
		try {
			// Create data input and output streams
			input = new DataInputStream(
					socket.getInputStream());
			output = new DataOutputStream(
					socket.getOutputStream());
			
			while (true) {
				//Get message from the client
				String message = input.readUTF();
				
				//Send message
				server.broadcast(message);
				
				//show message
				Platform.runLater(() -> {
					server.Connections.appendText(message + '\n');
				});
			}
		
			
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		try {
			socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	  }	

    }
	//send message back to client
	public void sendMessage (String message) {
		try {
			output.writeUTF(message);
			output.flush();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
