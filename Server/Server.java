import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        String serverMessage = "";
        String clientMessage;
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Połączono z klientem: " + clientSocket.getInetAddress());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                while ((clientMessage = in.readLine()) != null) {
                    if(clientMessage.contains("start"))
                    {
                        System.out.println("Klient: " + clientMessage.split(";")[1]);
                        serverMessage = clientMessage.split(";")[1];
                    }
                    out.println(serverMessage);
                }

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}