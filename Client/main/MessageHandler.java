package Client.main;

import DTOs.*;
import Server.game.cell.ShootingCell;
import Client.maps.Map;
import Server.game.map.ShootingMap;

import java.io.IOException;
import java.util.ArrayList;
/**
 * Handles incoming messages and objects from the server,
 * dispatching them to the appropriate UI update methods.
 */
public class MessageHandler {
    /**
     * Sends an object to the server using the client's output stream.
     *
     * @param object The object to send.
     */
    public static void sendObject(Object object) {
        try {
            Client.out.writeObject(object);
            Client.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handles a plain text message from the server,
     * adding it to the console and refreshing the display.
     *
     * @param message The message to handle.
     */
    public static void HandleMessage(String message)
    {
        Map.consoleMessages.add(message);
        Map.refreshConsole();
    }
    /**
     * Handles an incoming object from the server,
     * dispatching it to the appropriate handler based on its type.
     *
     * @param message The object received from the server.
     */
    public static void HandleObject(Object message)
    {
        if(message instanceof String)
        {
            HandleMessage((String)message);
        }
        if (message instanceof ShipPlacementRequestDto)
        {
            var shipPlacementRequestDto = (ShipPlacementRequestDto) message;
            Map.GeneratePlacingMap(shipPlacementRequestDto);
        }
        if(message instanceof ReceiveShotDto)
        {
            var receiveShotDto = (ReceiveShotDto) message;
            Map.GenerateComputerShootMap(receiveShotDto);
        }
        if (message instanceof ShootingMap)
        {
            var shootingMap = (ShootingMap) message;
            var cells = (ArrayList<ShootingCell>) shootingMap.get_cells();
            Map.GeneratePlayerShootMap(cells);
        }
        if(message instanceof ScoreboardDto){
            var scoreboardDto = (ScoreboardDto) message;
            Map.GenerateScoreboard(scoreboardDto);
        }
        if(message instanceof GameEndDto){
            var gameEndDto = (GameEndDto) message;
            Map.GenerateEndGame(gameEndDto);
        }
    }
}
