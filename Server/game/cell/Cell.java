package Server.game.cell;

import java.io.Serializable;

public abstract class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private final CellCoordinates coordinates;

    public Cell(int x, int y) {
        this.coordinates = new CellCoordinates(x, y);
    }

    public CellCoordinates getCoordinates() {
        return coordinates;
    }

    public int getX() {
        return coordinates.getX();
    }

    public int getY() {
        return coordinates.getY();
    }
}
