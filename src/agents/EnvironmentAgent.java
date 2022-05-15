package agents;

import OSPABA.*;
import entities.Customer;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.ArrayList;
import java.util.LinkedList;
//import instantAssistants.*;

//meta! id="2"
public class EnvironmentAgent extends Agent
{
	private LinkedList<Customer> customers;

	public EnvironmentAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		addOwnMessage(Mc.arrivingPerson);
		addOwnMessage(Mc.arrivingCar);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		customers = new LinkedList<>();
	}

	public LinkedList<Customer> getCustomers() {
		return customers;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new EnvironmentManager(Id.environmentManager, mySim(), this);
		new ArrivalCarsScheduler(Id.arrivalCarsScheduler, mySim(), this);
		new ArrivalPeopleScheduler(Id.arrivalPeopleScheduler, mySim(), this);
		addOwnMessage(Mc.initialization);
	}
	//meta! tag="end"
}