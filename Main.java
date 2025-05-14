import main.Client;

public static void main() {
    Thread serverThread = new Thread(() -> Server.main(new String[]{"8000"}));
    Thread clientThread = new Thread(() -> Client.main(new String[]{"localhost", "8000"}));
    serverThread.start();
    clientThread.start();
}