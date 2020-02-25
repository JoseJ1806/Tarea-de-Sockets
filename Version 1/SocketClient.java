import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClient {
	public static void main(String[] args) throws IOException {
		int port = 40000;
		String host = "127.0.0.1";
		Socket client = new Socket (host,port);
		OutputStreamWriter writer = new OutputStreamWriter (client.getOutputStream());
		writer.write("Hola\n");
		writer.flush();
		client.close();
	}
}
