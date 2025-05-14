package main;

import navigation.MainMenu;

import java.io.*;
import java.net.*;
import javax.swing.*;
public class Client {
    public static JFrame frame;
    public static BufferedReader in;
    public static PrintWriter out;
    public static void main(String[] args) {
        String serverMessage;
        try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]))) {
            frame = new JFrame("JAVA SHIP GAME");
            System.out.println("Połączono z serwerem");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            SwingUtilities.invokeLater(() -> MainMenu.displayMenu());
            while ((serverMessage = in.readLine()) != null) {
                if(serverMessage.contains("5")) {
                    frame.getContentPane().removeAll();
                    frame.revalidate();
                    frame.repaint();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}