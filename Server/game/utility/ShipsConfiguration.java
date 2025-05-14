package game.utility;

public class ShipsConfiguration {
    private final int fourCellShipsAmount;
    private final int threeCellShipsAmount;
    private final int twoCellShipsAmount;
    private final int oneCellShipsAmount;

    public ShipsConfiguration(int four, int three, int two, int one) {
        this.fourCellShipsAmount = four;
        this.threeCellShipsAmount = three;
        this.twoCellShipsAmount = two;
        this.oneCellShipsAmount = one;
    }

    /**
     * Returns the number of ships for each ship size,
     * ordered from the longest (4-cell) to the shortest (1-cell).
     *
     * @return an array of ship counts: [4-cell, 3-cell, 2-cell, 1-cell]
     */
    public int[] getShipAmounts() {
        return new int[] {
                fourCellShipsAmount,
                threeCellShipsAmount,
                twoCellShipsAmount,
                oneCellShipsAmount
        };
    }

    public int countAllShips() {
        return fourCellShipsAmount +
                threeCellShipsAmount +
                twoCellShipsAmount +
                oneCellShipsAmount;
    }
}