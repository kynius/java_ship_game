package Server.game.player;

import Server.game.cell.CellCoordinates;
import Server.game.cell.ShipsCell;
import Server.game.map.ShipsMap;
import Server.game.utility.Directions;

import java.util.ArrayList;
import java.util.List;

public class ShipPlacingManager {
    private ShipsMap _map;

    public ShipPlacingManager(ShipsMap map) {
        this._map = map;
    }

    public boolean placeShip(int shipLength, CellCoordinates startingCooridinate, Directions direction, int id)
    {
        if(shipLength == 1)
        {
            return addOneCellShip(startingCooridinate, id);
        } else
        {
            return addMultipeCellShip(startingCooridinate, direction, shipLength, id);
        }
    }

    private boolean addOneCellShip(CellCoordinates coordinates, int id)
    {
        List<ShipsCell> shipCells = new ArrayList<>();
        var isPossbile = isPossibleToShip(coordinates);
        if(isPossbile) {
            shipCells.add(_map.getCellAt(coordinates));
            markAsShip(shipCells, id);
            markSurroundingCellsBlocked(shipCells);
        }
        return isPossbile;
    }

    private boolean addMultipeCellShip(CellCoordinates start, Directions direction, int length, int id) {
        List<ShipsCell> shipCells = new ArrayList<>();

        int dx = 0;
        int dy = 0;

        switch (direction) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
        }

        for (int i = 0; i < length; i++) {
            int x = start.getX() + i * dx;
            int y = start.getY() + i * dy;
            CellCoordinates current = new CellCoordinates(x, y);

            if (!_map.areCoordinatesInBounds(current)) return false;

            ShipsCell cell = _map.getCellAt(current);
            if (cell == null || !cell.isPossibleToShip()) return false;

            shipCells.add(cell);
        }

        markAsShip(shipCells, id);
        markSurroundingCellsBlocked(shipCells);
        return true;
    }

    private void markSurroundingCellsBlocked(List<ShipsCell> shipCells) {
        for (ShipsCell shipCell : shipCells) {
            int baseX = shipCell.getX();
            int baseY = shipCell.getY();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;

                    int newX = baseX + dx;
                    int newY = baseY + dy;
                    CellCoordinates neighborCoords = new CellCoordinates(newX, newY);

                    if (_map.areCoordinatesInBounds(neighborCoords)) {
                        ShipsCell neighbor = _map.getCellAt(neighborCoords);
                        if (neighbor != null && neighbor.isPossibleToShip()) {
                            neighbor.setPossibleToShip(false);
                        }
                    }
                }
            }
        }
    }

    private void markAsShip(List<ShipsCell> cells, int id) {
        for (ShipsCell cell : cells) {
            cell.setShip(true);
            cell.setPossibleToShip(false);
            cell.setShipId(id);
        }
    }

    private boolean isPossibleToShip(CellCoordinates coordinates)
    {
        if(!_map.areCoordinatesInBounds(coordinates)) return false;
        var cell = _map.getCellAt(coordinates);
        return cell.isPossibleToShip();
    }
}
