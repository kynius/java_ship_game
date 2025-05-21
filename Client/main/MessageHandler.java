package Client.main;

import DTOs.ShipPlacementRequestDto;
import Server.game.cell.ShipsCell;
import Server.game.cell.ShootingCell;
import Client.maps.Map;

import java.io.IOException;
import java.util.ArrayList;

public class MessageHandler {
    public static void sendObject(Object object) {
        try {
            System.out.println(object);
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
            System.out.println("Ship Placement Request");
            var shipPlacementRequestDto = (ShipPlacementRequestDto) message;
            var shipmap = shipPlacementRequestDto.getShipmap();
            var cells = (ArrayList<ShipsCell>) shipmap.get_cells();
            Map.GeneratePlacingMap(cells);
        }
        if (message instanceof ArrayList<?> && ((ArrayList<?>) message).size() > 0&& ((ArrayList<?>) message).get(0) instanceof ShootingCell)
        {
            ArrayList<ShootingCell> cells = (ArrayList<ShootingCell>) message;
            System.out.println("Otrzymano listę ShipsCell:");
            cells.forEach(shootingCell -> {
                if(shootingCell.isShot() && shootingCell.isAimed()){
                    //trafiony
                }
                else if(shootingCell.isShot() && !shootingCell.isAimed()){
                    //pudło
                }
                else {
                    //nie strzelano
                }
            });
//            Map.GeneretateMap(cells);
        }
    }
}
