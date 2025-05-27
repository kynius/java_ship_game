package Server.game.player.humanPlayer;

import DTOs.ScoreDto;
import DTOs.ShipPlacementDto;
import DTOs.ShipPlacementRequestDto;
import RequestClasses.PauseStartRequest;
import RequestClasses.PauseStopRequest;
import RequestClasses.ScoreboardRequest;
import Server.Server;
import Server.game.ScoreboardHandler;
import Server.game.cell.CellCoordinates;
import Server.game.player.Player;
import Server.game.utility.ShipsConfiguration;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a human player in the game.
 * Handles interaction with the client via object messages and manages player input like shooting and ship placement.
 */
public class HumanPlayer extends Player {

    private CompletableFuture<CellCoordinates> pendingShot;
    private CompletableFuture<Void> pendingPlacement;

    private int shipsLeftToPlace;
    private int currentShipId;
    private int currentShipLength;
    private long pauseTime = 0;
    private long pauseStartTimestamp = 0;

    /**
     * Default constructor.
     */
    public HumanPlayer() {
    }

    /**
     * Constructs a human player with a given map size and ship configuration.
     *
     * @param mapSize the size of the game map
     * @param shipsConfiguration the configuration of ships
     * @throws IOException if sending initial state fails
     */
    public HumanPlayer(int mapSize, ShipsConfiguration shipsConfiguration) throws IOException {
        super(mapSize, shipsConfiguration);
    }

    /**
     * Initiates the shooting process for the player and returns a future to await player input.
     *
     * @return a future that completes when the player makes a move
     */
    @Override
    public CompletableFuture<CellCoordinates> makeShoot() {
        pendingShot = new CompletableFuture<>();
        _shotsMade++;
        sendMessage(_shootingMap);
        return pendingShot;
    }

    /**
     * Begins the ship placement process for the player.
     *
     * @return a future that completes when all ships are placed
     */
    @Override
    public CompletableFuture<Void> placeShips() {
        this.pendingPlacement = new CompletableFuture<>();
        this.shipsLeftToPlace = _shipsConfiguration.countAllShips();
        this.currentShipId = 1;
        this.currentShipLength = calculateShipLengthForIndex(0);
        sendCurrentShipRequest();
        return pendingPlacement;
    }

    /**
     * Processes the result of a shot the player made and updates the shooting map accordingly.
     *
     * @param shotStatus the result of the shot
     */
    @Override
    public void getShotInformationReturn(ShotStatus shotStatus) {
        if (shotStatus.getStatus() == ShotStatuses.SHOT || shotStatus.getStatus() == ShotStatuses.SHOTNDESTORYED) {
            var shootCell = _shootingMap.getCellAt(shotStatus.getShootCoordinate());
            shootCell.setIsAimed(true);
        }
        sendMessage(_shootingMap);
    }

    /**
     * Processes a shot received by the player and notifies the client.
     *
     * @param coordinates the coordinates that were shot
     * @return the result of the shot
     */
    @Override
    public ShotStatus takeShot(CellCoordinates coordinates) {
        ShotStatus result = super.takeShot(coordinates);
        var recieveShotDto = new DTOs.ReceiveShotDto(_shipsMap, coordinates);
        sendMessage(recieveShotDto);
        return result;
    }

    /**
     * Handles messages received from the client and dispatches them appropriately.
     *
     * @param message the message object sent by the client
     */
    public void handleMessage(Object message) {
        if (message instanceof CellCoordinates coords) {
            onPlayerShootReceived(coords);
        } else if (message instanceof ShipPlacementDto placement) {
            handleShipPlacementResponse(placement);
        } else if (message instanceof ScoreDto score) {
            ScoreboardHandler.addScore(score);
        } else if (message instanceof PauseStartRequest) {
            pauseStartTimestamp = System.currentTimeMillis();
        } else if (message instanceof PauseStopRequest) {
            if (pauseStartTimestamp > 0) {
                long now = System.currentTimeMillis();
                long pauseDuration = now - pauseStartTimestamp;
                pauseTime += pauseDuration / 1000;
                pauseStartTimestamp = 0;
            }
        } else if (message instanceof ScoreboardRequest) {
            var scoreboard = ScoreboardHandler.getSortedScoreboard();
            sendMessage(scoreboard);
        } else {
            throw new RuntimeException("Object not acceptable: " + message.getClass());
        }
    }

    /**
     * Processes a shot sent by the client and completes the pending shot future.
     *
     * @param coordinates the coordinates selected by the player
     */
    public void onPlayerShootReceived(CellCoordinates coordinates) {
        if (pendingShot != null && !pendingShot.isDone()) {
            var shotCell = _shootingMap.getCellAt(coordinates);
            shotCell.setShot(true);
            pendingShot.complete(coordinates);
        }
    }

    /**
     * Handles the response of a ship placement sent by the client.
     *
     * @param placementDto the placement details from the client
     */
    public void handleShipPlacementResponse(ShipPlacementDto placementDto) {
        if (shipsLeftToPlace <= 0) return;

        boolean placed = _shipPlacingManager.placeShip(
                currentShipLength,
                placementDto.getCoordinates(),
                placementDto.getDirection(),
                currentShipId
        );

        if (!placed) {
            sendMessage("Nie możesz tutaj postawić statku. Wychodzi poza mapę lub koliduje z innym statkiem");
            sendCurrentShipRequest();
            return;
        }

        shipsLeftToPlace--;
        currentShipId++;

        if (shipsLeftToPlace == 0) {
            sendMessage(_shipsMap);
            pendingPlacement.complete(null);
        } else {
            int placedSoFar = _shipsConfiguration.countAllShips() - shipsLeftToPlace;
            currentShipLength = calculateShipLengthForIndex(placedSoFar);
            sendCurrentShipRequest();
        }
    }

    /**
     * Sends the current ship placement request to the client.
     */
    private void sendCurrentShipRequest() {
        sendMessage(new ShipPlacementRequestDto(currentShipId, currentShipLength, _shipsMap));
    }

    /**
     * Calculates the correct ship length based on the ship index.
     *
     * @param shipIndex index of the ship being placed
     * @return the length of the ship
     */
    private int calculateShipLengthForIndex(int shipIndex) {
        int[] amounts = _shipsConfiguration.getShipAmounts();
        int cumulative = 0;
        for (int i = 0; i < amounts.length; i++) {
            cumulative += amounts[i];
            if (shipIndex < cumulative) {
                return amounts.length - i;
            }
        }
        throw new IllegalStateException("Invalid ship placement tracking");
    }

    /**
     * Sends a serializable object to the client.
     *
     * @param object the object to send
     */
    public void sendMessage(Object object) {
        try {
            Server.out.reset();
            Server.out.writeObject(object);
            Server.out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send object to client: " + e.getMessage());
        }
    }

    /**
     * Sends a string message to the client.
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        try {
            Server.out.reset();
            Server.out.writeObject(message);
            Server.out.flush();
            Thread.sleep(1);
        } catch (IOException e) {
            System.err.println("Failed to send message to client: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }

    /**
     * Returns the total pause time accumulated by the player during the game.
     *
     * @return the total paused time in seconds
     */
    public long getPauseTimeInSeconds() {
        return pauseTime;
    }
}
