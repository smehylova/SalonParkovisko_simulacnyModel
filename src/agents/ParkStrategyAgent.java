package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="96"
public class ParkStrategyAgent extends Agent
{
	public ParkStrategyAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
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
		new ParkStrategyManager(Id.parkStrategyManager, mySim(), this);
		addOwnMessage(Mc.createStrategy);
	}
	//meta! tag="end"
}