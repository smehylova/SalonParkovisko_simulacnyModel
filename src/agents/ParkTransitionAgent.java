package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="82"
public class ParkTransitionAgent extends Agent
{
	public ParkTransitionAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		addOwnMessage(Mc.createStrategy);
		addOwnMessage(Mc.parking);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ParkTransitionManager(Id.parkTransitionManager, mySim(), this);
		new FromSalonToCarParkProcess(Id.fromSalonToCarParkProcess, mySim(), this);
		new ParkingProcess(Id.parkingProcess, mySim(), this);
		addOwnMessage(Mc.goToNextQueue);
		addOwnMessage(Mc.nextQueue);
		addOwnMessage(Mc.goToStartPlace);
		addOwnMessage(Mc.startParking);
		addOwnMessage(Mc.fromSalonToCarPark);
		addOwnMessage(Mc.nextPlace);
	}
	//meta! tag="end"
}