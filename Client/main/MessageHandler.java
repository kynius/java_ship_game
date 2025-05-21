package Client.main;

import Server.game.cell.ShipsCell;
import Server.game.cell.ShootingCell;
import Client.maps.Map;

import java.util.ArrayList;

public class MessageHandler {
    public static void HandleMessage(String message)
    {
        System.out.println(message);
    }
    public static void HandleObject(Object message)
    {
        if(message instanceof String)
        {
            HandleMessage((String)message);
        }
        if (message instanceof ArrayList<?> && ((ArrayList<?>) message).size() > 0 && ((ArrayList<?>) message).get(0) instanceof ShipsCell)
        {
            ArrayList<ShipsCell> cells = (ArrayList<ShipsCell>) message;
            cells.forEach(shipsCell -> {
            });
            System.out.println("Otrzymano listę ShipsCell:");
            Map.GeneretateMap(cells);
        }
        if (message instanceof ArrayList<?> && ((ArrayList<?>) message).size() > 0 && ((ArrayList<?>) message).get(0) instanceof ShootingCell)
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
