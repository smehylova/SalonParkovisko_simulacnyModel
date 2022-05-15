package entities;

import OSPABA.Entity;
import OSPABA.Simulation;
import simulation.MySimulation;

public class Customer extends Entity {
    private boolean driver;
    private String parkingStatus;
    private int parkingPlaceX = -1;
    private int parkingPlaceY = -1;
    private String parkingPlace = "";
    private int parkingSuccess = 0;

    private String status;

    private boolean hair = false;
    private boolean clean = false;
    private boolean consmetic = false;

    private double startTime;
    private double startQueueTime = 0;
    private double waitingTime;

    private double goingToSalonDistance = 0;
    private double goingToSalonSpeed = 0;

    private boolean isRemoved = false;

    public Customer(Simulation mySim) {
        super(mySim);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getStartTime() {
        return startTime;
    }

    public boolean isHair() {
        return hair;
    }

    public void setHair(boolean hair) {
        this.hair = hair;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public boolean isConsmetic() {
        return consmetic;
    }

    public void setConsmetic(boolean consmetic) {
        this.consmetic = consmetic;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getGoingToSalonDistance() {
        return goingToSalonDistance;
    }

    public void setGoingToSalonDistance(double goingToSalonTime) {
        this.goingToSalonDistance = goingToSalonTime;
    }

    public void setStartQueueTime(double startQueueTime) {
        this.startQueueTime = startQueueTime;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void endOfWaitingTimeQueue() {
        this.waitingTime = mySim().currentTime() - this.startQueueTime;
    }

    public int getParkingPlaceX() {
        return parkingPlaceX;
    }

    public void setParkingPlaceX(int parkingPlaceX) {
        this.parkingPlaceX = parkingPlaceX;
    }

    public int getParkingPlaceY() {
        return parkingPlaceY;
    }

    public void setParkingPlaceY(int parkingPlaceY) {
        this.parkingPlaceY = parkingPlaceY;
    }

    public int getParkingSuccess() {
        return parkingSuccess;
    }

    public void addParkingSuccess(int parkingSuccess) {
        this.parkingSuccess += parkingSuccess;
    }

    public String getParkingStatus() {
        return parkingStatus;
    }

    public void setParkingStatus(String parkingStatus) {
        this.parkingStatus = parkingStatus;
    }

    public double getGoingToSalonSpeed() {
        return goingToSalonSpeed;
    }

    public void setGoingToSalonSpeed(double goingToSalonSpeed) {
        this.goingToSalonSpeed = goingToSalonSpeed;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public String getParkingPlace() {
        return parkingPlace;
    }

    public void setParkingPlace(String parkingPlace) {
        this.parkingPlace = parkingPlace;
    }
}
