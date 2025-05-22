package Server.game;

import DTOs.GameEndDto;
import Server.game.player.Player;
import Server.game.player.computerPlayer.ComputerPlayer;
import Server.game.player.humanPlayer.HumanPlayer;
import Server.game.utility.MapConfigfuration;
import Server.game.utility.ShotStatuses;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Game {

    private final Random _random = new Random();
    private final Player[] _players = new Player[2];
    private boolean _wasFirstMoveDone = false;
    private LocalDateTime _gameStartTime;

    public Game(Player playerOne, Player playerTwo, MapConfigfuration mapConfig ) {

        if (_random.nextBoolean()) {
            _players[0] = playerOne;
            _players[1] = playerTwo;
        } else {
            _players[0] = playerTwo;
            _players[1] = playerOne;
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
        if (_players[0] instanceof HumanPlayer && !_wasFirstMoveDone) {
            ((HumanPlayer) _players[0]).sendMessage("Zaczynasz pierwszy, zmarkuj swoj strzal");
        }  else if(!_wasFirstMoveDone){
            ((HumanPlayer) _players[1]).sendMessage("Przeciwnik zaczyna, czekaj na swoją kolej");
        }
        _wasFirstMoveDone = true;
        if (_gameStartTime == null) {
            _gameStartTime = LocalDateTime.now();
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
                    if (_players[0] instanceof HumanPlayer) {
                        ((HumanPlayer) _players[0]).sendMessage("Koniec gry, wygrałeś");
                        endGame(true);
                    } else {
                        ((HumanPlayer) _players[1]).sendMessage("Koniec gry, przegrałeś");
                        endGame(false);
                    }
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
            if((_players[0] instanceof HumanPlayer
                    && (shootResult.getStatus() == ShotStatuses.SHOT || shootResult.getStatus() == ShotStatuses.SHOTNDESTORYED))
                    || _players[0] instanceof ComputerPlayer)
            {
                _players[0].getShotInformationReturn(shootResult);
            }
            if(_players[0] instanceof ComputerPlayer && (shootResult.getStatus() == ShotStatuses.SHOT || shootResult.getStatus() == ShotStatuses.SHOTNDESTORYED))
            {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            playGame();
        });
    }
    private void endGame(boolean isHumanWinner) {
        HumanPlayer humanPlayer = null;
        for (Player player : _players) {
            if (player instanceof HumanPlayer) {
                humanPlayer = (HumanPlayer) player;
                break;
            }
        }
        long gameTimeInSeconds = Duration.between(_gameStartTime, LocalDateTime.now()).getSeconds();
        int movesMadeByYou = humanPlayer.getShotsMade();

        GameEndDto gameEndDto = new GameEndDto(
                isHumanWinner,
                gameTimeInSeconds,
                movesMadeByYou
        );

//        humanPlayer.sendMessage(gameEndDto);
    }
}
