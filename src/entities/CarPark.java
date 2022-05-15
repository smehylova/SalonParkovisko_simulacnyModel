package entities;

import OSPABA.Entity;
import OSPABA.Simulation;

public class CarPark extends Entity {

    private int countPlacesInRow = 15;
    private int penalValue = 50;
    private int countRows = 3;
    private int[][] parkingPlaces;

    public CarPark(Simulation mySim) {
        super(mySim);
    }

    public int getCountPlacesInRow() {
        return countPlacesInRow;
    }

    public int getPenalValue() {
        return penalValue;
    }

    public int getCountRows() {
        return countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public int[][] getParkingPlaces() {
        return parkingPlaces;
    }

    public void setParkingPlaces(int[][] parkingPlaces) {
        this.parkingPlaces = parkingPlaces;
    }
}
