package Server.game.player.computerPlayer;

import Server.game.cell.CellCoordinates;
import Server.game.map.ShootingMap;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;

import java.util.Random;

/**
 * Handles shooting logic for the computer player.
 * Alternates between random shooting and targeted destruction using {@link DestroyMechanism}.
 */
public class ComputerShootingManager {

    private final ShootingMap shootingMap;
    private final ShootingMap cellsLeftToShoot;
    private DestroyMechanism destroyMechanism;
    private final Random random = new Random();
    private final ShotStatus lastShotStatus;
    private boolean isDestroying = false;

    /**
     * Creates a new manager for computer shooting behavior.
     *
     * @param shootingMap the computer's shooting map
     * @param lastShotStatus the result of the last shot made
     */
    public ComputerShootingManager(ShootingMap shootingMap, ShotStatus lastShotStatus) {
        this.shootingMap = shootingMap;
        this.cellsLeftToShoot = new ShootingMap(shootingMap);
        this.lastShotStatus = lastShotStatus;
        this.destroyMechanism = new DestroyMechanism(cellsLeftToShoot, shootingMap, this.lastShotStatus);
    }

    /**
     * Sets whether the computer is currently attempting to destroy a partially revealed ship.
     *
     * @param isDestroying true if destroy mode should be active
     */
    public void setIsDestroying(boolean isDestroying) {
        this.isDestroying = isDestroying;
    }

    /**
     * Executes a shot based on the current strategy (random or destroy mode).
     *
     * @return the coordinates selected for the shot
     */
    public CellCoordinates shoot() {
        if (!isDestroying) {
            return shootRandomCell();
        } else {
            return destroyMechanism.destroy();
        }
    }

    /**
     * Selects and marks a random valid target cell for shooting.
     *
     * @return coordinates of the cell to shoot
     */
    private CellCoordinates shootRandomCell() {
        if (lastShotStatus.getStatus() == ShotStatuses.SHOTNDESTORYED) {
            cellsLeftToShoot.removeCellsAround(lastShotStatus.getShootCoordinate());
        }

        CellCoordinates target = getRandomTargetCoordinates();
        removeFromShootable(target);
        shootingMap.getCellAt(target).setShot(true);
        return target;
    }

    /**
     * Returns a randomly selected coordinate from the available targets.
     *
     * @return coordinates to target
     */
    private CellCoordinates getRandomTargetCoordinates() {
        int index = random.nextInt(cellsLeftToShoot.get_cells().size());
        return cellsLeftToShoot.getCellAtIndex(index).getCoordinates();
    }

    /**
     * Removes a coordinate from the list of shootable cells.
     *
     * @param coordinates the coordinates to remove
     */
    private void removeFromShootable(CellCoordinates coordinates) {
        cellsLeftToShoot.removeCellAt(coordinates);
    }

    /**
     * Resets the destroyer to a new state and disables destroy mode.
     */
    public void resetDestroyer() {
        isDestroying = false;
        destroyMechanism.removeNeighborsOfShipToBeDestroyed();
        destroyMechanism = new DestroyMechanism(cellsLeftToShoot, shootingMap, lastShotStatus);
    }
}
