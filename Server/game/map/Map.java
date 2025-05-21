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
                coordinates.getX() < _size - 1&&
                coordinates.getY() > 0 &&
                coordinates.getY() < _size - 1;
    }

    protected abstract void initialize(int size);
}