package Client.main;

import Client.maps.Map;
import Client.navigation.MainMenu;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;
public class Client {
    public static JFrame frame;
    public static ObjectOutputStream out;
    public static void main(String[] args) {
        ObjectInputStream in;
        Object message;
        try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]))) {
            Map.consoleMessages = new ArrayList<String>();
            frame = new JFrame("JAVA SHIP GAME");
            System.out.println("Połączono z serwerem");
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