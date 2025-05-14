package game.cell;

public abstract class Cell {
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
