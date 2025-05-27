package Client.main;

import Client.maps.Map;
import Client.navigation.MainMenu;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;
/**
 * The Client class is responsible for connecting to the game server,
 * initializing the main application window, and handling incoming messages.
 */
public class Client {
    /**
     * The main application window frame.
     */
    public static JFrame frame;
    /**
     * The output stream used to send objects to the server.
     */
    public static ObjectOutputStream out;
    /**
     * The main entry point for the client application.
     * Connects to the server, initializes the GUI, and processes incoming messages.
     *
     * @param args Command line arguments: server address and port.
     */
    public static void main(String[] args) {
        ObjectInputStream in;
        Object message;
        try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]))) {
            Map.consoleMessages = new ArrayList<String>();
            frame = new JFrame("JAVA SHIP GAME");
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            SwingUtilities.invokeLater(() -> MainMenu.displayMenu());
            while ((message = in.readObject()) != null) {
                MessageHandler.HandleObject(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}