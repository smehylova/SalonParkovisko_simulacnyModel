package managers;

import OSPABA.*;
import entities.Customer;
import simulation.*;
import agents.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="2"
public class EnvironmentManager extends Manager
{
	public EnvironmentManager(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="BossAgent", id="12", type="Notice"
	public void processInitialization(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.arrivalPeopleScheduler));
		//message.setCode(Mc.arrivingPerson);
		startContinualAssistant(message);

		MessageForm msg = message.createCopy();
		msg.setAddressee(myAgent().findAssistant(Id.arrivalCarsScheduler));
		startContinualAssistant(msg);
	}

	//meta! sender="ArrivalCarsScheduler", id="63", type="Finish"
	public void processFinishArrivalCarsScheduler(MessageForm message)
	{
		arrivalCustomer(true, message);
	}

	//meta! sender="ArrivalPeopleScheduler", id="11", type="Finish"
	public void processFinishArrivalPeopleScheduler(MessageForm message)
	{
		arrivalCustomer(false, message);
	}

	public void arrivalCustomer(boolean driver, MessageForm message) {
		Customer customer = new Customer(mySim());
		customer.setDriver(driver);
		myAgent().getCustomers().add(customer);
		((MyMessage)message).setCustomer(customer);

		message.setCode(Mc.arrivalCustomer);
		message.setAddressee(mySim().findAgent(Id.bossAgent));
		notice(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.initialization:
			processInitialization(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.arrivalCarsScheduler:
				processFinishArrivalCarsScheduler(message);
			break;

			case Id.arrivalPeopleScheduler:
				processFinishArrivalPeopleScheduler(message);
			break;
			}
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public EnvironmentAgent myAgent()
	{
		return (EnvironmentAgent)super.myAgent();
	}

}