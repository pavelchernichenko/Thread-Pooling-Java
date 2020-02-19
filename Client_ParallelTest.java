import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client_ParallelTest {

	public static void main(String[] args) throws Exception {

		Client[] clients = new Client[200];
		for (int x = 0; x < clients.length; x++) {
			clients[x] = new Client();
			clients[x].start();

		}

		for (int x = 0; x < clients.length; x++) {
			clients[x].join();
		}

		System.out.println("Single Command: Tests done");
		String serverAddress = "localhost";
		Socket socket = new Socket(serverAddress, 9898);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		in.readLine();
		out.println("KILL");
		socket.close();

	}

	public static class Client extends Thread {
		private BufferedReader in;
		private PrintWriter out;
		Socket socket;
		String[] commands = { "ADD,4,5", "MUL,6,8", "DIV,50,5", "SUB,53425,2345", "ADD,455,4355", "MUL,6346,84365",
				"DIV,543650,7655", "SUB,765425,6575" };

		public void run() {
			Random random = new Random();
			String response;
			int messagesToSend = 1;
			try {
				connectToServer();
				String welcome = in.readLine();
				System.out.print(welcome + "\n");
				if (!welcome.equals("Server busy try again later.")) {
					for (int x = 0; x < messagesToSend; x++) {
						String command = commands[random.nextInt(commands.length)];
						out.println(command);

						try {
							response = in.readLine();
						} catch (IOException ex) {
							response = "Error: " + ex;
						}
						System.out.println("Command: " + command + ", Response: " + response);
					}
				}
			} catch (IOException e) {
			}

		}

		public void connectToServer() throws IOException {

			// Get the server address from a dialog box.
			String serverAddress = "localhost";

			// Make connection and initialize streams
			socket = new Socket(serverAddress, 9898);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// Consume the initial welcoming messages from the server
		}

	}

}
