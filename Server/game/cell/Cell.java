package game.cell;

import java.io.Serializable;

public abstract class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private final CellCoordinates coordinates;
    private boolean isHit;

    public Cell(int x, int y) {
        this.coordinates = new CellCoordinates(x, y);
        this.isHit = false;
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

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean isHit) {
        this.isHit = isHit;
    }
}
