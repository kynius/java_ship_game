package Client.main;

import DTOs.*;
import Server.game.cell.ShootingCell;
import Client.maps.Map;
import Server.game.map.ShootingMap;

import java.io.IOException;
import java.util.ArrayList;

public class MessageHandler {
    public static void sendObject(Object object) {
        try {
            Client.out.writeObject(object);
            Client.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void HandleMessage(String message)
    {
        Map.consoleMessages.add(message);
        Map.refreshConsole();
    }
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
