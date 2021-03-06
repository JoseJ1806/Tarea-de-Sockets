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
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * This is the client which passes and gets messages to the server and also with multiple clients
 * 
 * It uses TaskReadThread.java file in order to get simultaneous input from the server
 *
 */

public class SocketClient extends Application{

	TextField puerto;
	static TextField txtInput;
	public TextArea messages = new TextArea();
	
	DataOutputStream output = null;
	
	String Negro_Mensajes = "-fx-background-color: rgba(66, 66, 66, 5);";
	String Negro_TypeBar = "-fx-control-inner-background: rgba(66, 66, 66, 5);";
	String Amarillo_Letra = "-fx-text-fill: rgba(249, 226, 162, 0.9);";
	String Fondo = "-fx-background-color: rgba(249, 226, 162, 0.9);";
	/**
	 * This where all the elements of the GUI are initialized
	 * 
	 * 
	 * 
	 * @return root where all the GUI elements are located (inside a BorderPane)
	 */
	public Parent createContent() {
		
		messages.setMaxSize(600,600);
		messages.setStyle("-fx-control-inner-background: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9);");
		messages.setMouseTransparent(true);
		messages.setFocusTraversable(false);
		messages.setEditable(false);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(messages);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		
		txtInput = new TextField();
		txtInput.setStyle("-fx-background-color: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9)");
		txtInput.setMinSize(510,80);
		txtInput.setPromptText("Mensaje");
		
		puerto = new TextField();
		puerto.setStyle("-fx-background-color: rgba(66, 66, 66, 5); -fx-text-fill: rgba(249, 226, 162, 0.9)");
		puerto.setMinSize(30,80);
		puerto.setPromptText("Destinatario");
		
		
		Button enviar = new Button("Enviar");
		enviar.setMinSize(70, 80);
		enviar.setStyle("-fx-font:bold italic 24pt Arial; -fx-text-fill:rgba(249, 226, 162, 0.9); -fx-background-color: rgba(66, 66, 66, 5);");
		enviar.setOnAction(new ButtonListener());
		
		HBox espacio = new HBox(3,puerto,txtInput,enviar);
		HBox.setHgrow(txtInput, Priority.ALWAYS);
				
		BorderPane root = new BorderPane();
		root.setBottom(espacio);
		root.setCenter(messages);
		BorderPane.setAlignment(messages,Pos.CENTER);
		BorderPane.setAlignment(espacio, Pos.BOTTOM_LEFT);
		root.setPrefSize(400, 400);
		root.setStyle(Fondo);
		return root;
			
	}
	
	/**
	 * 
	 * This is where the GUI is created by calling the Parent createContent
	 *
	 */
	
	@Override
	public void start (Stage primaryStage) throws Exception {
		primaryStage.setTitle("Text Chat App");
		primaryStage.setScene(new Scene(createContent(),800,600));
		primaryStage.show();
		primaryStage.setResizable(true);
	
		try {
            // Create a socket to connect to the server
            Socket socket = new Socket(ConnectionUtil.host , ConnectionUtil.port);
       
            //Connection successful
            messages.appendText("Connected. \n");
          
            // Create an output stream to send data to the server
            output = new DataOutputStream(socket.getOutputStream());

            //create a thread in order to read message from server continuously
            TaskReadThread task = new TaskReadThread(socket, this);
            Thread thread = new Thread(task);
            thread.start();
        } catch (IOException ex) {
            
            messages.appendText(ex.toString() + '\n');
        }
	}
	public static void main(String[] args) {
		launch (args);	
	}
	
	/**
	 * Button from which the client sends messages
	 *@param contacto The client's user name which will be displayed in the conversation
	 * @param message The client's input from his side of the conversation
	 *
	 */
	
	private class ButtonListener implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent e) {
			try {
				String contacto = puerto.getText().trim();
				String message = txtInput.getText().trim();
				
				if (contacto.length() == 0) {
					contacto = "Unknown";
				}
				if (message.length() == 0) {
                    return;
				}
				//send message 
				output.writeUTF("[" + contacto + "]: " + message + "");
				output.flush();
				
				//clear the txtInput TextField
				txtInput.clear();
			} catch(IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
