package Server.game;

import Server.game.player.Player;
import Server.game.player.computerPlayer.ComputerPlayer;
import Server.game.player.humanPlayer.HumanPlayer;
import Server.game.utility.MapConfigfuration;
import Server.game.utility.ShotStatuses;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Game {

    private final Random _random = new Random();
    private final MapConfigfuration _mapConfig;
    private final Player[] _players = new Player[2];

    public Game(Player playerOne, Player playerTwo, MapConfigfuration mapConfig) {
        _mapConfig = mapConfig;

        Player human = new HumanPlayer(_mapConfig.getMapSize(), _mapConfig.getShipsConfiguration());
        Player computer = new ComputerPlayer(_mapConfig.getMapSize(), _mapConfig.getShipsConfiguration());

        if (_random.nextBoolean()) {
            _players[0] = human;
            _players[1] = computer;
        } else {
            _players[0] = computer;
            _players[1] = human;
        }
    }

    private void switchTurn() {
        Player temp = _players[0];
        _players[0] = _players[1];
        _players[1] = temp;
    }

    private CompletableFuture<Void> placeShips() {
        return CompletableFuture.allOf(
                _players[0].placeShips(),
                _players[1].placeShips()
        );
    }

    public void startGame() {
        placeShips().thenRun(this::playGame);
    }

    private void playGame() {
        _players[0].makeShoot().thenAccept(shootCoordinates -> {
            var shootResult = _players[1].takeShot(shootCoordinates);
            _players[0].getShotInformationReturn(shootResult);

            CompletableFuture<Void> messageDelay;

            if (shootResult.getStatus() == ShotStatuses.SHOTNDESTORYED) {
                if (_players[0] instanceof HumanPlayer) {
                    messageDelay = sendMessageToClientWithDelay("Trafiłeś i zatopiłeś okręt przeciwnika, wybierz kolejne pole");
                } else {
                    messageDelay = CompletableFuture.completedFuture(null);
                }

                if (_players[1].getShipsLeftInGame() <= 0) {
                    endGame();
                    return;
                }

            } else if (shootResult.getStatus() == ShotStatuses.SHOT) {
                if (_players[0] instanceof HumanPlayer) {
                    messageDelay = sendMessageToClientWithDelay("Trafiłeś, wybierz kolejne pole");
                } else {
                    messageDelay = CompletableFuture.completedFuture(null);
                }

            } else { // MISSED
                if (_players[0] instanceof HumanPlayer) {
                    messageDelay = sendMessageToClientWithDelay("Chybiłeś, przeciwnik celuje");
                } else {
                    messageDelay = CompletableFuture.completedFuture(null);
                }
                switchTurn();
            }

            messageDelay.thenRun(this::playGame);
        });
    }

    private void endGame() {
        // TODO: handle end game logic
    }

    private void SendMessegeToClient(String msg) {
        // TODO: Implement actual client message sending
    }

    private CompletableFuture<Void> sendMessageToClientWithDelay(String msg) {
        SendMessegeToClient(msg);
        return CompletableFuture.runAsync(() -> {}, CompletableFuture.delayedExecutor(5, java.util.concurrent.TimeUnit.SECONDS));
    }
}
