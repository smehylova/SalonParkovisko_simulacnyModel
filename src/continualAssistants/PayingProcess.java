package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="49"
public class PayingProcess extends Process
{
	private UniformContinuousRNG uniConPayingGenerator;
	public PayingProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		uniConPayingGenerator = new UniformContinuousRNG(130.0, 230.0);
	}

	//meta! sender="ReceptionAgent", id="50", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.paying);
		hold(uniConPayingGenerator.sample(), message);
	}

	public void paying(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.paying:
				paying(message);
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