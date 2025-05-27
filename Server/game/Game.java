package Server.game;

import DTOs.GameEndDto;
import Server.game.player.Player;
import Server.game.player.humanPlayer.HumanPlayer;
import Server.game.utility.MapConfigfuration;
import Server.game.utility.ShotStatus;
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

    public Game(Player playerOne, Player playerTwo, MapConfigfuration mapConfig) {
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
        if (!_wasFirstMoveDone) {
            sendFirstMoveInfo(_players[0]);
            _wasFirstMoveDone = true;
        }

        if (_gameStartTime == null) {
            _gameStartTime = LocalDateTime.now();
        }

        _players[0].makeShoot().thenAccept(shootCoordinates -> {
            ShotStatus shootResult = _players[1].takeShot(shootCoordinates);
            boolean gameEnded = handleShotResponse(_players[0], shootResult);

            if (gameEnded) {
                endGame(_players[0] instanceof HumanPlayer);
                return;
            }

            if (shootResult.getStatus() == ShotStatuses.MISSED) {
                switchTurn();
            }

            playGame();
        });
    }

    private void sendFirstMoveInfo(Player currentPlayer) {
        if (currentPlayer instanceof HumanPlayer) {
            ((HumanPlayer) currentPlayer).sendMessage("Zaczynasz pierwszy, zmarkuj swoj strzal");
        } else {
            ((HumanPlayer) _players[1]).sendMessage("Przeciwnik zaczyna, czekaj na swoją kolej");
        }
    }

    private boolean handleShotResponse(Player currentPlayer, ShotStatus shotStatus) {
        ShotStatuses status = shotStatus.getStatus();

        switch (status) {
            case SHOTNDESTORYED -> {
                if (currentPlayer instanceof HumanPlayer) {
                    ((HumanPlayer) currentPlayer).sendMessage("Trafiłeś i zatopiłeś okręt, oddaj kolejny strzał");
                } else {
                    ((HumanPlayer) _players[1]).sendMessage("Przeciwnik trafił i zatopił twój okręt... Przeciwnik strzela");
                    sleep(1500);
                }

                currentPlayer.getShotInformationReturn(shotStatus);

                if (_players[1].getShipsLeftInGame() <= 0) {
                    sendGameEndMessage(currentPlayer);
                    return true;
                }
            }
            case SHOT -> {
                if (currentPlayer instanceof HumanPlayer) {
                    ((HumanPlayer) currentPlayer).sendMessage("Trafiłeś , oddaj kolejny strzał");
                } else {
                    ((HumanPlayer) _players[1]).sendMessage("Przeciwnik trafił... Przeciwnik strzela");
                    sleep(1500);
                }

                currentPlayer.getShotInformationReturn(shotStatus);
            }
            case MISSED -> {
                if (currentPlayer instanceof HumanPlayer) {
                    ((HumanPlayer) currentPlayer).sendMessage("Chybiłeś... Przeciwnik strzela");
                } else {
                    ((HumanPlayer) _players[1]).sendMessage("Przeciwnik chybił, twoja kolej");
                    sleep(1500);
                }

                currentPlayer.getShotInformationReturn(shotStatus);
            }
        }

        return false;
    }

    private void sendGameEndMessage(Player winner) {
        if (winner instanceof HumanPlayer) {
            ((HumanPlayer) winner).sendMessage("Koniec gry, wygrałeś");
        } else {
            ((HumanPlayer) _players[1]).sendMessage("Koniec gry, przegrałeś");
        }
    }

    private void endGame(boolean isHumanWinner) {
        HumanPlayer humanPlayer = null;
        for (Player player : _players) {
            if (player instanceof HumanPlayer) {
                humanPlayer = (HumanPlayer) player;
                break;
            }
        }
        long gameDuration = Duration.between(_gameStartTime, LocalDateTime.now()).toSeconds();
        long gameTimeInSeconds = gameDuration - humanPlayer.getPauseTimeInSeconds();
        int movesMadeByYou = humanPlayer.getShotsMade();

        GameEndDto gameEndDto = new GameEndDto(
                isHumanWinner,
                gameTimeInSeconds,
                movesMadeByYou
        );

        humanPlayer.sendMessage(gameEndDto);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}
