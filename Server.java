import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
        System.out.println("Server is running.");

        ThreadManager manager = new ThreadManager();
        manager.start();

        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new ClientHandler(manager, listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class ClientHandler extends Thread {
        private ThreadManager manager;
        private Socket socket;
        private int clientNumber;

        public ClientHandler(ThreadManager manager, Socket socket, int clientNumber) {
            this.manager = manager;
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client #" + clientNumber + " at " + socket);
        }

        public void run() {
            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Hello, you are client #" + clientNumber + ".");

                boolean shouldContinue = true;
                while (shouldContinue) {
                    String input = in.readLine();
                    shouldContinue = manager.addJob(input, out);
                }

            } catch (Exception e) {
                log("Error handling client #" + clientNumber + ": " + e.toString());
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client #" + clientNumber + " closed");
            }
        }

        private void log(String message) {
            System.out.println(message);
        }
    }
}
