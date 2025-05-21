package game.cell;

import java.io.Serializable;
import java.util.Objects;

public class CellCoordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int x;
    private final int y;

    public CellCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CellCoordinates that = (CellCoordinates) obj;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}