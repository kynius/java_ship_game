package Server.game.cell;

import java.io.Serializable;

public class ShootingCell extends Cell implements Serializable {
    private boolean isShot = false;
    private boolean isAimed = false;

    public ShootingCell(int x, int y) {
        super(x, y);
    }

    public boolean isShot() {
        return isShot;
    }

    public void setShot(boolean isShot) {
        this.isShot = isShot;
    }

    public boolean isAimed() {
        return isAimed;
    }

    public void setIsAimed(boolean isAimed) {
        this.isAimed = isAimed;
    }
}
