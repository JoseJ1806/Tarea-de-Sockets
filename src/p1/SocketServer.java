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

public class SocketServer extends Application {
	
	 Alert bad_port = new Alert(AlertType.NONE);
	TextField puerto;
	TextFormatter<Integer> numeros;
	Stage primaryStage;
	ScrollPane scrollPane;
	Scene PrimeraVentana;
    public TextArea Connections;
    List<TaskClientConnection> connectionList = new ArrayList<TaskClientConnection>();

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
    	
    	puerto = new TextField();
    	puerto.setStyle("-fx-background-color: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9)");
    	puerto.setMaxSize(200, 50);
    	puerto.setPromptText("Escriba aqui el puerto");
    	puerto.setTextFormatter(numeros);
    	
    	Text Titulo = new Text("Inicio");
    	Titulo.setFill(Color.BLACK);
    	Titulo.setFont(new Font(24));
    	Titulo.setWrappingWidth(150);
    	Titulo.setTextAlignment(TextAlignment.LEFT);
    	Titulo.setUnderline(true);
    	
    	Button login = new Button("Iniciar");
    	login.setMaxSize(75, 50);
    	login.setStyle("-fx-font:bold italic 12pt Arial; -fx-text-fill:rgba(249, 226, 162, 0.9); -fx-background-color: rgba(66, 66, 66, 5);");
    	login.setOnAction(new ButtonAction());
    	
    	HBox hbox = new HBox (10,Titulo,login);
 
    	BorderPane Ventana = new BorderPane();
    	Ventana.setStyle("-fx-background-color: rgba(249, 226, 162, 0.9)");
    	Ventana.setCenter(puerto);
    	Ventana.setTop(hbox);
    	BorderPane.setAlignment(puerto, Pos.CENTER);
    	BorderPane.setAlignment(hbox, Pos.TOP_LEFT);
  
    	PrimeraVentana = new Scene(Ventana,250,120);
    	primaryStage.setTitle("Digite su puerto");
    	primaryStage.setScene(PrimeraVentana);
    	primaryStage.show();
    	primaryStage.setResizable(false);
    	
    	//Scene scene = new Scene(scrollPane,450,500);
    	//primaryStage.setTitle("Server:Text Chat App");
    	//primaryStage.setScene(scene);
    	//primaryStage.show();
    	//primaryStage.setResizable(false);
    	    	
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
    
    private class ButtonAction implements EventHandler <ActionEvent>{

		@Override
		public void handle(ActionEvent a) {
			
			Stage mainstage = new Stage();
			String conexion = puerto.getText().trim();
			ConnectionUtil.port = Integer.parseInt(conexion);
			
			if (Integer.parseInt(conexion) <= 4000) {
				bad_port.setAlertType(AlertType.ERROR);
				bad_port.setContentText("Por favor digite otro puerto");
				bad_port.show();
				
			}
			
			Scene scene = new Scene(scrollPane,450,500);
	    	mainstage.setTitle("Server connections");
	    	mainstage.setScene(scene);
	    	mainstage.show();
	    	mainstage.setResizable(false);
	    	primaryStage.hide();
	    	
	    	System.out.println(ConnectionUtil.port);
	    	System.out.println(Integer.parseInt(conexion));
		}
    	
    }
}		

