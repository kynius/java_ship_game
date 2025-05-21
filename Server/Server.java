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

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            HumanPlayer humanPlayer = new HumanPlayer();
            while (true) {
                Object inputObject = in.readObject();
                if (inputObject instanceof MapConfigfuration config) {

                    ComputerPlayer computerPlayer = new ComputerPlayer(config.getMapSize(), config.getShipsConfiguration());
                    humanPlayer = new HumanPlayer(config.getMapSize(), config.getShipsConfiguration());

                    Game game = new Game(humanPlayer, computerPlayer, config);
                    game.startGame();
                }else {
                    humanPlayer.handleMessage(inputObject);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
