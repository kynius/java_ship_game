package Server.game.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import Server.game.cell.Cell;
import Server.game.cell.CellCoordinates;

public abstract class Map<T extends Cell> implements Serializable {
    private static final long serialVersionUID = 1L;
    protected List<T> _cells;
    private int _size;
    public Map(){

    }
    public Map(int size) {
        this._cells = new ArrayList<>();
        initialize(size);
        this._size = size;
    }

    public List<T> get_cells() {
        return _cells;
    }

    public void addCell(T cell) {
        _cells.add(cell);
    }

    public int getSize() {
        return _size;
    }

    public T getCellAt(CellCoordinates coordinates) {
        for (T cell : _cells) {
            if (cell.getCoordinates().equals(coordinates)) {
                return cell;
            }
        }
        return null;
    }

    public boolean areCoordinatesInBounds(CellCoordinates coordinates) {
        return coordinates.getX() > 0 &&
                coordinates.getX() <= _size &&
                coordinates.getY() > 0 &&
                coordinates.getY() <= _size;
    }


    public void removeCellAt(CellCoordinates coordinates) {
        _cells.removeIf(cell -> cell.getCoordinates().equals(coordinates));
    }

    public void removeCellsAround(CellCoordinates center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // skip center cell
                CellCoordinates neighbor = new CellCoordinates(center.getX() + dx, center.getY() + dy);
                removeCellAt(neighbor);
            }
        }
    }

    public T getCellAtIndex(int index) {
        if (index < 0 || index >= _cells.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        return _cells.get(index);
    }
    
    protected abstract void initialize(int size);
}