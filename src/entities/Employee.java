package entities;

import OSPABA.Entity;
import OSPABA.Simulation;

public class Employee extends Entity {
    private boolean isOccupied = false;
    private double work = 0;
    private double startWork = -1;

    public Employee(Simulation mySim) {
        super(mySim);
    }

    public Employee(int id, Simulation mySim) {
        super(id, mySim);
    }

    public double getUtilization() {
        return (this.work / mySim().currentTime()) * 100;
    }

    public void addWord(double _work) {
        this.work += _work;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public double getWork() {
        return work;
    }

    public double getStartWork() {
        return startWork;
    }

    public void setStartWork(double startWork) {
        this.startWork = startWork;
    }
}
