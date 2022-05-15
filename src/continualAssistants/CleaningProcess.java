package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="54"
public class CleaningProcess extends Process
{
	private TriangularRNG cleanGenerator;
	public CleaningProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		cleanGenerator = new TriangularRNG(360.0, 540.0, 900.0);
	}

	//meta! sender="CosmeticAgent", id="55", type="Start"
	public void processStart(MessageForm message)
	{
		((MyMessage) message).getCustomer().setStatus("Process of cleaning");

		message.setCode(Mc.cleaning);
		hold(cleanGenerator.sample(), message);
	}

	public void cleaning(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.cleaning:
				cleaning(message);
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
	public CosmeticAgent myAgent()
	{
		return (CosmeticAgent)super.myAgent();
	}

}