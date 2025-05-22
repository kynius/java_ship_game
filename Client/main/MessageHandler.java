package Client.main;

import DTOs.ShipPlacementRequestDto;
import Server.game.cell.ShipsCell;
import Server.game.cell.ShootingCell;
import Client.maps.Map;
import Server.game.map.ShipsMap;
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
            var shipmap = shipPlacementRequestDto.getShipmap();
            var cells = (ArrayList<ShipsCell>) shipmap.get_cells();
            Map.GeneratePlacingMap(cells);
        }
        if(message instanceof ShipsMap)
        {
            var shipmap = (ShipsMap) message;
            var cells = (ArrayList<ShipsCell>) shipmap.get_cells();
            Map.GenerateComputerShootMap(cells);
        }
        if (message instanceof ShootingMap)
        {
            var shootingMap = (ShootingMap) message;
            var cells = (ArrayList<ShootingCell>) shootingMap.get_cells();
            Map.GeneratePlayerShootMap(cells);
        }
    }
}
