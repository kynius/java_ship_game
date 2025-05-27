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

/**
 * Represents a game between two players. Handles turn order, shooting logic, game flow, and ending.
 */
public class Game {

    private final Random _random = new Random();
    private final Player[] _players = new Player[2];
    private boolean _wasFirstMoveDone = false;
    private LocalDateTime _gameStartTime;

    /**
     * Initializes the game with two players and a map configuration.
     * Randomly assigns which player starts first.
     *
     * @param playerOne the first player
     * @param playerTwo the second player
     * @param mapConfig the configuration of the map
     */
    public Game(Player playerOne, Player playerTwo, MapConfigfuration mapConfig) {
        if (_random.nextBoolean()) {
            _players[0] = playerOne;
            _players[1] = playerTwo;
        } else {
            _players[0] = playerTwo;
            _players[1] = playerOne;
        }
    }

    /**
     * Switches the current turn between players.
     */
    private void switchTurn() {
        Player temp = _players[0];
        _players[0] = _players[1];
        _players[1] = temp;
    }

    /**
     * Asynchronously places ships for both players.
     *
     * @return a CompletableFuture that completes when both players have placed their ships
     */
    private CompletableFuture<Void> placeShips() {
        return CompletableFuture.allOf(
                _players[0].placeShips(),
                _players[1].placeShips()
        );
    }

    /**
     * Starts the game by triggering the ship placement and game loop.
     */
    public void startGame() {
        placeShips().thenRun(this::playGame);
    }

    /**
     * Manages the core gameplay loop, including handling shots, switching turns,
     * and checking for game end.
     */
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

    /**
     * Sends an initial message to the human player indicating turn order.
     *
     * @param currentPlayer the player who will start
     */
    private void sendFirstMoveInfo(Player currentPlayer) {
        if (currentPlayer instanceof HumanPlayer) {
            ((HumanPlayer) currentPlayer).sendMessage("Zaczynasz pierwszy, zmarkuj swoj strzal");
        } else {
            ((HumanPlayer) _players[1]).sendMessage("Przeciwnik zaczyna, czekaj na swoją kolej");
        }
    }

    /**
     * Handles the result of a shot and informs the players of the outcome.
     *
     * @param currentPlayer the player who made the shot
     * @param shotStatus the result of the shot
     * @return true if the game has ended, false otherwise
     */
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

    /**
     * Sends a game-end message to the appropriate player.
     *
     * @param winner the player who won
     */
    private void sendGameEndMessage(Player winner) {
        if (winner instanceof HumanPlayer) {
            ((HumanPlayer) winner).sendMessage("Koniec gry, wygrałeś");
        } else {
            ((HumanPlayer) _players[1]).sendMessage("Koniec gry, przegrałeś");
        }
    }

    /**
     * Ends the game, calculates game statistics and sends a GameEndDto to the human player.
     *
     * @param isHumanWinner true if the human player won, false otherwise
     */
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

    /**
     * Pauses execution for the specified number of milliseconds.
     *
     * @param ms duration in milliseconds to sleep
     */
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}
