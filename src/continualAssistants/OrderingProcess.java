package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="47"
public class OrderingProcess extends Process
{
	private UniformContinuousRNG uniConOrderingGenerator;
	public OrderingProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		uniConOrderingGenerator = new UniformContinuousRNG(80.0, 320.0);
	}

	//meta! sender="ReceptionAgent", id="48", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.ordering);
		hold(uniConOrderingGenerator.sample(), message);
	}

	public void ordering(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.ordering:
				ordering(message);
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
	public ReceptionAgent myAgent()
	{
		return (ReceptionAgent)super.myAgent();
	}

}