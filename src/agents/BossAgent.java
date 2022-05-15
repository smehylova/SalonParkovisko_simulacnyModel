package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="1"
public class BossAgent extends Agent
{
	public BossAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		addOwnMessage(Mc.initialization);
		addOwnMessage(Mc.closeSalon);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	public void startSimulation() {
		MyMessage message = new MyMessage(mySim());
		//message.setAddressee(Id.environmentAgent);
		message.setCode(Mc.initialization);
		manager().notice(message);

		MyMessage msg = new MyMessage(mySim());
		msg.setAddressee(findAssistant(Id.salonIsOpenProcess));
		manager().startContinualAssistant(msg);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new BossManager(Id.bossManager, mySim(), this);
		new SalonIsOpenProcess(Id.salonIsOpenProcess, mySim(), this);
		addOwnMessage(Mc.leaveDriver);
		addOwnMessage(Mc.parkCustomer);
		addOwnMessage(Mc.arrivalCustomer);
	}
	//meta! tag="end"
}