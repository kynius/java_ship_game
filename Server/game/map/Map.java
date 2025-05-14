package game.map;

import java.util.ArrayList;
import java.util.List;
import game.cell.Cell;
import game.cell.CellCoordinates;

public abstract class Map<T extends Cell> {
    protected List<T> _cells;
    private int _size;

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