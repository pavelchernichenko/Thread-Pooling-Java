import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client_CLI {

  private BufferedReader in;
  private PrintWriter out;
  
  public void connectToServer() throws IOException {
    Scanner stdin = new Scanner(System.in);

    // Get the server address from a dialog box.
    System.out.print("Enter IP Address of the Server: ");
    String serverAddress = stdin.nextLine();

    // Make connection and initialize streams
    Socket socket = new Socket(serverAddress, 9898);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);

    while (true) {
      System.out.println(in.readLine());
      System.out.print("> ");

      String command = stdin.nextLine();
      out.println(command);
      if (command.equals("KILL"))
        break;
    }

    in.close();
    out.close();
    socket.close();
    stdin.close();
  }

  public static void main(String[] args) throws Exception {
    Client_CLI client = new Client_CLI();
    client.connectToServer();
  }
}