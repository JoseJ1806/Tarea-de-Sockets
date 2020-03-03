package p1;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;



public class SocketServer extends Application {
    public TextArea Connections;
    List<TaskClientConnection> connectionList = new ArrayList<TaskClientConnection>();

    @Override
	public void start (Stage primaryStage) {
    	Connections = new TextArea();
    	Connections.setEditable(false);
    	Connections.setStyle("-fx-control-inner-background: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9);");
    	
    	ScrollPane scrollPane = new ScrollPane();
    	scrollPane.setContent(Connections);
    	scrollPane.setFitToHeight(true);
    	scrollPane.setFitToWidth(true);
    	
    	Scene scene = new Scene(scrollPane,450,500);
    	primaryStage.setTitle("Server:Text Chat App");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	
    	
    	//create a new thread
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
    public void broadcast(String message) {
    	for (TaskClientConnection clientConnection : this.connectionList) {
    		clientConnection.sendMessage(message);
    	}
    }
}		

