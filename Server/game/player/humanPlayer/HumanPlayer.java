package Server.game.player.humanPlayer;

import DTOs.ShipPlacementDto;
import DTOs.ShipPlacementRequestDto;
import Server.Server;
import Server.game.cell.CellCoordinates;
import Server.game.player.Player;
import Server.game.utility.ShipsConfiguration;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;

import java.io.*;
import java.util.concurrent.CompletableFuture;

public class HumanPlayer extends Player { ;

    private CompletableFuture<CellCoordinates> pendingShot;
    private CompletableFuture<Void> pendingPlacement;

    private int shipsLeftToPlace;
    private int currentShipId;
    private int currentShipLength;

    public HumanPlayer(){

    }

    public HumanPlayer(int mapSize, ShipsConfiguration shipsConfiguration) throws IOException {
        super(mapSize, shipsConfiguration);
    }

    @Override
    public CompletableFuture<CellCoordinates> makeShoot() {
        pendingShot = new CompletableFuture<>();
        sendMessage(_shootingMap);
        return pendingShot;
    }

    @Override
    public CompletableFuture<Void> placeShips() {
        this.pendingPlacement = new CompletableFuture<>();
        this.shipsLeftToPlace = _shipsConfiguration.countAllShips();
        this.currentShipId = 1;
        this.currentShipLength = calculateShipLengthForIndex(0); // first ship
        sendCurrentShipRequest();
        return pendingPlacement;
    }

    @Override
    public void getShotInformationReturn(ShotStatus shotStatus) {
        if(shotStatus.getStatus() == ShotStatuses.SHOT || shotStatus.getStatus() == ShotStatuses.SHOTNDESTORYED) {
            var shootCell = _shootingMap.getCellAt(shotStatus.getShootCoordinate());
            shootCell.setIsAimed(true);
        }
        sendMessage(_shootingMap);
    }

    @Override
    public ShotStatus takeShot(CellCoordinates coordinates) {
        ShotStatus result = super.takeShot(coordinates);
        sendMessage(_shipsMap);
        return result;
    }

    public void handleMessage(Object message) {
        if (message instanceof CellCoordinates coords) {
            onPlayerShootReceived(coords);
        } else if (message instanceof ShipPlacementDto placement) {
            handleShipPlacementResponse(placement);
        } else {
            throw new RuntimeException("Object not acceptable: " + message.getClass());
        }
    }

    public void onPlayerShootReceived(CellCoordinates coordinates) {
        if (pendingShot != null && !pendingShot.isDone()) {
            var shotCell = _shootingMap.getCellAt(coordinates);
            shotCell.setShot(true);
            pendingShot.complete(coordinates);
        }
    }

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

    private void sendCurrentShipRequest() {
        sendMessage(new ShipPlacementRequestDto(currentShipId, currentShipLength, _shipsMap));
    }

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

    private void sendMessage(Object object) {
        try {
            Server.out.reset();
            Server.out.writeObject(object);
            Server.out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send object to client: " + e.getMessage());
        }
    }

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
}

