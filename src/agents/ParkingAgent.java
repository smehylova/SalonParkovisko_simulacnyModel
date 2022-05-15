package agents;

import OSPABA.*;
import OSPStat.Stat;
import entities.CarPark;
import entities.Customer;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.LinkedList;
//import instantAssistants.*;

//meta! id="3"
public class ParkingAgent extends Agent
{
	private LinkedList<Customer> drivers;
	private int countDrivers;
	private int countSuccessDrivers;

	private Stat statParkingSuccess;

	private CarPark carPark;

	public ParkingAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		addOwnMessage(Mc.parking);
	}

	@Override
	public void prepareReplication()
	{
		statParkingSuccess = new Stat();

		carPark = new CarPark(mySim());

		carPark.setParkingPlaces(new int[((MySimulation) mySim()).getParkCountRows()][carPark.getCountPlacesInRow()]);
		for (int i = 0; i < ((MySimulation) mySim()).getParkCountRows(); i++) {
			for (int j = 0; j < carPark.getCountPlacesInRow(); j++) {
				carPark.getParkingPlaces()[i][j] = -1;
			}
		}
		carPark.setCountRows(((MySimulation) mySim()).getParkCountRows());

		drivers = new LinkedList<>();
		countDrivers = 0;
		countSuccessDrivers = 0;

		super.prepareReplication();
		// Setup component for the next replication
	}

	public Stat getStatParkingSuccess() {
		return statParkingSuccess;
	}

	public LinkedList<Customer> getDrivers() {
		return drivers;
	}

	public CarPark getCarPark() {
		return carPark;
	}

	public int getCountDrivers() {
		return countDrivers;
	}

	public void addCountDrivers() {
		this.countDrivers++;
	}

	public int getCountSuccessDrivers() {
		return countSuccessDrivers;
	}

	public void addCountSuccessDrivers() {
		this.countSuccessDrivers++;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ParkingManager(Id.parkingManager, mySim(), this);
		addOwnMessage(Mc.goToNextQueue);
		addOwnMessage(Mc.nextQueue);
		addOwnMessage(Mc.goToStartPlace);
		addOwnMessage(Mc.startParking);
		addOwnMessage(Mc.fromSalonToCarPark);
		addOwnMessage(Mc.nextPlace);
		addOwnMessage(Mc.createStrategy);
		addOwnMessage(Mc.freePlace);
		addOwnMessage(Mc.parkCustomer);
	}
	//meta! tag="end"
}