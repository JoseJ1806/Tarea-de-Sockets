package p1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/** This is the server class
* 
* 
* 
* It uses the TaskClientConnection.java file which represents each new connection
*/

public class SocketServer extends Application {
	
    Stage primaryStage;
    ScrollPane scrollPane;
    public TextArea Connections;
    static List<TaskClientConnection> connectionList = new ArrayList<TaskClientConnection>();
	
    /**
    * 
    * All of the server's GUI elements go here
    * 
    */
   
    @Override
	public void start (Stage primaryStage) {
    	this.primaryStage = primaryStage;
    	Connections = new TextArea();
    	Connections.setEditable(false);
    	Connections.setStyle("-fx-control-inner-background: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9);");
    	
    	scrollPane = new ScrollPane();
    	scrollPane.setContent(Connections);
    	scrollPane.setFitToHeight(true);
    	scrollPane.setFitToWidth(true);
    	
		Scene scene = new Scene(scrollPane,450,500);
    	primaryStage.setTitle("Server connections");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	primaryStage.setResizable(false);
 
    	    	
    	 /**
    	 * This thread allows for the creation of the serverSocket and continuous connections to it
    	 */
    	new Thread(() -> {
				try {
					//Creates a server socket
					ServerSocket serverSocket = new ServerSocket(ConnectionUtil.port);
    			
					//appends a message inside the server TextArea showing when a new client has connected to the server
					Platform.runLater(()
    				-> Connections.appendText("New server started at" + new Date() + '\n'));
    			
					//continuous loop inside the server
					while (true) {
						//Listen for a connection request, add new connection to the list
						Socket socket = serverSocket.accept();
						TaskClientConnection connection = new TaskClientConnection(socket, this);
						connectionList.add(connection);
    			
						Thread thread = new Thread(connection);
						thread.start();
    			}
    			
    		} catch (IOException ex) {
    			Connections.appendText(ex.toString() + '\n');
    			
    		} 
    			
    		

			
    	}).start();
    	
    }

    public static void main(String[] args) {
    	launch(args);
    }
    
    //send message to all connected clients
    public static void broadcast(String message) {
    	for (TaskClientConnection clientConnection : connectionList) {
    		clientConnection.sendMessage(message);
    	}
    }
}



		
    	
  
		

