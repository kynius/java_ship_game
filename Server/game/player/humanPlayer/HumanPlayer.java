package Server.game.player.humanPlayer;

import DTOs.ShipPlacementDto;
import DTOs.ShipPlacementRequestDto;
import Server.game.cell.CellCoordinates;
import Server.game.player.Player;
import Server.game.utility.ShipsConfiguration;
import Server.game.utility.ShotStatus;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class HumanPlayer extends Player {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private CompletableFuture<CellCoordinates> pendingShot;
    private CompletableFuture<Void> pendingPlacement;

    private int shipsLeftToPlace;
    private int currentShipId;
    private int currentShipLength;

    public HumanPlayer(int mapSize, ShipsConfiguration shipsConfiguration, Socket socket) throws IOException {
        super(mapSize, shipsConfiguration);
        this.socket = socket;

        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        startListening();
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
        sendMessage(_shootingMap);
    }

    @Override
    public ShotStatus takeShot(CellCoordinates coordinates) {
        ShotStatus result = super.takeShot(coordinates); // call base logic
        sendMessage(_shipsMap);
        return result;
    }

    private void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    Object message = in.readObject();
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Connection lost or invalid object: " + e.getMessage());
            }
        }).start();
    }

    private void handleMessage(Object message) {
        if (message instanceof CellCoordinates coords) {
            onPlayerShootReceived(coords);
        } else if (message instanceof ShipPlacementDto placement) {
            handleShipPlacementResponse(placement);
        } else {
            throw new RuntimeException("Object not acceptable: " + message.getClass());
        }
    }

    private void onPlayerShootReceived(CellCoordinates coordinates) {
        if (pendingShot != null && !pendingShot.isDone()) {
            pendingShot.complete(coordinates);
        }
    }

    private void handleShipPlacementResponse(ShipPlacementDto placementDto) {
        if (shipsLeftToPlace <= 0) return;

        boolean placed = _shipPlacingManager.placeShip(
                currentShipLength,
                placementDto.getCoordinates(),
                placementDto.getDirection(),
                currentShipId
        );

        if (!placed) {
            sendMessage("SHIP_PLACEMENT_FAILED: Cannot place ship at that location.");
            return;
        }

        shipsLeftToPlace--;
        currentShipId++;

        if (shipsLeftToPlace == 0) {
            pendingPlacement.complete(null);
        } else {
            int placedSoFar = _shipsConfiguration.countAllShips() - shipsLeftToPlace;
            currentShipLength = calculateShipLengthForIndex(placedSoFar);
            sendCurrentShipRequest();
        }
    }

    private void sendCurrentShipRequest() {
        sendMessage(new ShipPlacementRequestDto(currentShipId, currentShipLength));
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
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send object to client: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
            Thread.sleep(5000); // wait 5 seconds after sending
        } catch (IOException e) {
            System.err.println("Failed to send message to client: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // good practice to restore interrupt flag
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}

