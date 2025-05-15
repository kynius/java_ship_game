package Server.game.player.humanPlayer;

import Server.game.cell.CellCoordinates;
import Server.game.player.Player;
import Server.game.utility.ShipsConfiguration;
import Server.game.utility.ShotStatus;

import java.util.concurrent.CompletableFuture;

public class HumanPlayer extends Player {
    public HumanPlayer(int mapSize, ShipsConfiguration shipsConfiguration) {
        super(mapSize, shipsConfiguration);
    }

    @Override
    public CompletableFuture<CellCoordinates> makeShoot() {
        _shotsMade++;
        return CompletableFuture.completedFuture(new CellCoordinates(2, 2));
    }

    @Override
    public CompletableFuture<Void> placeShips() {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void getShotInformationReturn(ShotStatus shotStatus) {
    }
}
