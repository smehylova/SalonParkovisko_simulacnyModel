package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="138"
public class CleanHairProcess extends Process
{
	public CleanHairProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="HairAgent", id="139", type="Start"
	public void processStart(MessageForm message)
	{
		((MyMessage) message).getCustomer().setStatus("CLEANING hair");
		message.setCode(Mc.cleaningHair);
		hold(10*60, message);
	}

	public void cleaningHair(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.cleaningHair:
				cleaningHair(message);
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
	public HairAgent myAgent()
	{
		return (HairAgent)super.myAgent();
	}

}
