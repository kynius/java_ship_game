import Server.Server;
import Client.main.Client;


public class Main {
    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> Server.main(new String[]{"8000"}));
        Thread clientThread = new Thread(() -> Client.main(new String[]{"localhost", "8000"}));
        serverThread.start();
        clientThread.start();
    }
}