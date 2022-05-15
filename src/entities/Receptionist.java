package entities;

import OSPABA.Entity;
import OSPABA.Simulation;

public class Receptionist extends Employee {
    private char typeOfWork = 'x';//x=nic, p=platba, r=recepcia

    public Receptionist(int id, Simulation mySim) {
        super(id, mySim);
    }

    public char getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(char typeOfWork) {
        this.typeOfWork = typeOfWork;
    }
}
