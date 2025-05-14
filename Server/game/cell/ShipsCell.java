package game.cell;

public class ShipsCell extends Cell {
    private boolean isShip = false;
    private boolean isPossibleToShip = true;
    private boolean isHit = false;
    private Integer shipId = null;

    public ShipsCell(int x, int y) {
        super(x, y);
    }

    public boolean isShip() {
        return isShip;
    }

    public void setShip(boolean isShip) {
        this.isShip = isShip;
    }

    public boolean isPossibleToShip() {
        return isPossibleToShip;
    }

    public void setPossibleToShip(boolean isPossibleToShip) {
        this.isPossibleToShip = isPossibleToShip;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean isHit) {
        this.isHit = isHit;
    }

    public Integer getShipId() {
        return this.shipId;
    }
}