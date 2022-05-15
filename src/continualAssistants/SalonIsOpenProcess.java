package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="129"
public class SalonIsOpenProcess extends Process
{
	public SalonIsOpenProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="BossAgent", id="130", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.closeSalon);
		hold(((MySimulation) mySim()).getMaxTimeOfSalon(), message);
	}

	public void closeSalon(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.closeSalon:
				closeSalon(message);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public BossAgent myAgent()
	{
		return (BossAgent)super.myAgent();
	}

}