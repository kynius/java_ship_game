package Server;

import Server.game.Game;
import Server.game.player.computerPlayer.ComputerPlayer;
import Server.game.player.humanPlayer.HumanPlayer;
import Server.game.utility.MapConfigfuration;

import java.io.*;
import java.net.*;

public class Server {
    public static ObjectOutputStream out;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            System.out.println("Serwer nasłuchuje na porcie: " + args[0]);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Połączono z klientem: " + clientSocket.getInetAddress());

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            while (true) {
                Object inputObject = in.readObject();

                if (inputObject instanceof MapConfigfuration config) {
                    System.out.println("▶ Otrzymano konfigurację mapy od klienta");

                    ComputerPlayer computerPlayer = new ComputerPlayer(config.getMapSize(), config.getShipsConfiguration());
                    HumanPlayer humanPlayer = new HumanPlayer(config.getMapSize(), config.getShipsConfiguration(), clientSocket);
                    Game game = new Game(humanPlayer, computerPlayer, config);
                    game.startGame();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
