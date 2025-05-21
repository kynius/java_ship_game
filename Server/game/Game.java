package Server.game;

import Server.game.player.Player;
import Server.game.player.humanPlayer.HumanPlayer;
import Server.game.utility.MapConfigfuration;
import Server.game.utility.ShotStatuses;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Game {

    private final Random _random = new Random();
    private final Player[] _players = new Player[2];
    private boolean _wasFirstMoveDone = false;

    public Game(Player playerOne, Player playerTwo, MapConfigfuration mapConfig ) {
        _players[0] = playerTwo;
        _players[1] = playerOne;

        //        if (_random.nextBoolean()) {
//            _players[0] = playerOne;
//            _players[1] = playerTwo;
//        } else {
//            _players[0] = playerTwo;
//            _players[1] = playerOne;
//        }
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
        if (_players[0] instanceof HumanPlayer && _wasFirstMoveDone) {
            ((HumanPlayer) _players[0]).sendMessage("Zaczynasz pierwszy, zmarkuj swoj strzal");
        }  else {
            ((HumanPlayer) _players[1]).sendMessage("Przeciwnik zaczyna, czekaj na swoją kolej");
        }

        _players[0].makeShoot().thenAccept(shootCoordinates -> {

            var shootResult = _players[1].takeShot(shootCoordinates);


            if (shootResult.getStatus() == ShotStatuses.SHOTNDESTORYED) {
                if (_players[0] instanceof HumanPlayer) {
                    ((HumanPlayer) _players[0]).sendMessage("Trafiłeś i zatopiłeś okręt, oddaj kolejny strzał");
                } else {
                    ((HumanPlayer) _players[1]).sendMessage("Przeciniwk trafił i zatopił twój okręt... Przeciniwk strzela");
                }

                if (_players[1].getShipsLeftInGame() <= 0) {
                    endGame();
                    return;
                }

            } else if (shootResult.getStatus() == ShotStatuses.SHOT) {
                if (_players[0] instanceof HumanPlayer) {
                    ((HumanPlayer) _players[0]).sendMessage("Trafiłeś , oddaj kolejny strzał");
                } else {
                    ((HumanPlayer) _players[1]).sendMessage("Przeciniwk trafił... Przeciniwk strzela");
                }

            } else { // MISSED
                if (_players[0] instanceof HumanPlayer) {
                    ((HumanPlayer) _players[0]).sendMessage("Chybiłeś... Przeciwnik strzela");
                } else {
                    ((HumanPlayer) _players[1]).sendMessage("Przeciwnik chybił, twoja kolej ");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Sleep interrupted: " + e.getMessage());
                    }
                }
                switchTurn();
            }

            _players[0].getShotInformationReturn(shootResult);
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
