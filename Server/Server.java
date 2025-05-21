package Server;

import Server.game.cell.ShipsCell;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    public static ObjectOutputStream out;
    public static void main(String[] args) {
        Object clientMessage;
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Połączono z klientem: " + socket.getInetAddress());
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject("Witaj na serwerze");
                // wydmuszka mapy
                var cells = new ArrayList<ShipsCell>();
                for (int x = 1; x <= 10; x++) {
                    for (int y = 1; y <= 10; y++) {
                        cells.add(new ShipsCell(x, y));
                    }
                }
                Thread.sleep(2000);
                out.writeObject(cells);
                while ((clientMessage = in.readObject()) != null) {

                }
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}