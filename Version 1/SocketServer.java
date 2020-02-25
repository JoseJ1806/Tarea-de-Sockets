import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	public static void main (String[] args) {
		boolean active = true;
		try {
			int port = 40000;
			ServerSocket serverSocket = new ServerSocket (port);
			while (active) {
				System.out.println("listening...");
				Socket entrante = serverSocket.accept();
				
				BufferedReader lector = new BufferedReader(new InputStreamReader(entrante.getInputStream()));
				String mensaje = lector.readLine();
				System.out.println ("Mensaje recibido:" + mensaje);
				entrante.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
				
		}
	}

}
