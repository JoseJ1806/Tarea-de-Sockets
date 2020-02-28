package p1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
//import javafx.scene.paint.Color;
import javafx.stage.Stage;
//import javafx.scene.layout.Region;

public class UserInterface extends Application{
	
	String Negro_Mensajes = "-fx-background-color: rgba(66, 66, 66, 5);";
	String Negro_TypeBar = "-fx-control-inner-background: rgba(66, 66, 66, 5);";
	String Amarillo_Letra = "-fx-text-fill: rgba(249, 226, 162, 0.9);";
	
	private TextArea messages = new TextArea();
	
	
	private Parent createContent() {
		
		messages.setMaxSize(400,400);

		messages.setStyle(Negro_TypeBar);
		messages.setMouseTransparent(true);
		messages.setFocusTraversable(false);
		messages.setEditable(false);
		
		TextField input = new TextField();
		input.setStyle(Negro_Mensajes);
		input.setMinSize(650,80);
		
		Button enviar = new Button("Enviar");
		enviar.setMinSize(155, 80);
		enviar.setStyle(Amarillo_Letra);
		enviar.setStyle(Negro_Mensajes);
		enviar.setStyle("-fx-font:bold italic 24pt Arial; -fx-text-fill:rgba(249, 226, 162, 0.9); -fx-background-color: rgba(66, 66, 66, 5);");
		enviar.setOnAction(arg0);

		HBox espacio = new HBox(5,input,enviar);
				
		BorderPane root = new BorderPane();
		root.setBottom(espacio);
		root.setCenter(messages);
		BorderPane.setAlignment(messages,Pos.TOP_RIGHT);
		BorderPane.setAlignment(espacio, Pos.BOTTOM_LEFT);
		
		root.setPrefSize(400, 400);
		String fondo =  "-fx-background-color: rgba(249, 226, 162, 0.9);";
		root.setStyle(fondo);
		return root;
			
	}
	
	@Override
	public void start (Stage primaryStage) throws Exception {
		primaryStage.setTitle("Prueba");
		primaryStage.setScene(new Scene(createContent(),800,600));
		primaryStage.show();
		primaryStage.setResizable(false);
	}
	public static void main(String[] args) {
		launch (args);	
	}
}
