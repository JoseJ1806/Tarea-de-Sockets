package p1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



public class SocketClient extends Application{

	TextField puerto;
	TextField input;
	public TextArea messages = new TextArea();
	
	DataOutputStream output = null;
	
	String Negro_Mensajes = "-fx-background-color: rgba(66, 66, 66, 5);";
	String Negro_TypeBar = "-fx-control-inner-background: rgba(66, 66, 66, 5);";
	String Amarillo_Letra = "-fx-text-fill: rgba(249, 226, 162, 0.9);";
	String Fondo = "-fx-background-color: rgba(249, 226, 162, 0.9);";
		
	public Parent createContent() {
		
		messages.setMaxSize(400,400);
		messages.setStyle("-fx-control-inner-background: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9);");
		messages.setMouseTransparent(true);
		messages.setFocusTraversable(false);
		messages.setEditable(false);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(messages);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		
		TextField input = new TextField();
		input.setStyle("-fx-background-color: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9)");
		input.setMinSize(510,80);
		input.setPromptText("Mensaje");
		
		TextField puerto = new TextField();
		puerto.setStyle("-fx-background-color: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9)");
		puerto.setMinSize(30,80);
		puerto.setPromptText("Destinatario");
		
		
		Button enviar = new Button("Enviar");
		enviar.setMinSize(70, 80);
		enviar.setStyle(Amarillo_Letra);
		enviar.setStyle(Negro_Mensajes);
		enviar.setStyle("-fx-font:bold italic 24pt Arial; -fx-text-fill:rgba(249, 226, 162, 0.9); -fx-background-color: rgba(66, 66, 66, 5);");
		enviar.setOnAction(new ButtonListener());
		
		HBox espacio = new HBox(3,puerto,input,enviar);
				
		BorderPane root = new BorderPane();
		root.setBottom(espacio);
		root.setCenter(messages);
		BorderPane.setAlignment(messages,Pos.TOP_RIGHT);
		BorderPane.setAlignment(espacio, Pos.BOTTOM_LEFT);
		root.setPrefSize(400, 400);
		root.setStyle(Fondo);
		return root;
			
	}
	
	@Override
	public void start (Stage primaryStage) throws Exception {
		primaryStage.setTitle("Prueba");
		primaryStage.setScene(new Scene(createContent(),800,600));
		primaryStage.show();
		primaryStage.setResizable(false);
	
		try {
            // Create a socket to connect to the server
            Socket socket = new Socket(ConnectionUtil.host, ConnectionUtil.port);

            //Connection successful
            messages.appendText("Connected. \n");
          
            // Create an output stream to send data to the server
            output = new DataOutputStream(socket.getOutputStream());

            //create a thread in order to read message from server continuously
            //TaskReadThread task = new TaskReadThread(socket, this);
            //Thread thread = new Thread(task);
            //thread.start();
        } catch (IOException ex) {
            
            messages.appendText(ex.toString() + '\n');
        }
	}
	public static void main(String[] args) {
		launch (args);	
	}
	
	private class ButtonListener implements EventHandler<ActionEvent>{
		
		@Override
		public void handle(ActionEvent e) {
			try {
				String contacto = puerto.getText().trim();
				String mensaje = input.getText().trim();
				
				if (contacto.length() == 0) {
					contacto = "Unknown";
				}
				if (mensaje.length() == 0) {
                    return;
				}
				//send message 
				output.writeUTF("[" + contacto + "]:" + mensaje + "");
				output.flush();
				
				//clear the input TextField
				input.clear();
			} catch(IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
