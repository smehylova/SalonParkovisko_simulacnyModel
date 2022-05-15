package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="56"
public class CosmeticProcess extends Process
{
	private UniformContinuousRNG cosmeticGenerator;
	private UniformDiscreteRNG easyCosmeticGenerator;
	private UniformDiscreteRNG hardCosmeticGenerator;

	public CosmeticProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		cosmeticGenerator = new UniformContinuousRNG(0.0, 1.0);
		easyCosmeticGenerator = new UniformDiscreteRNG(10, 25);
		hardCosmeticGenerator = new UniformDiscreteRNG(20, 100);
	}

	//meta! sender="CosmeticAgent", id="57", type="Start"
	public void processStart(MessageForm message)
	{
		double randCosmetic = cosmeticGenerator.sample();
		int duration;
		if (randCosmetic < 0.3) {
			duration = easyCosmeticGenerator.sample() * 60;
			((MyMessage) message).getCustomer().setStatus("Process of easy cosmetic");
		} else {
			duration = hardCosmeticGenerator.sample() * 60;
			((MyMessage) message).getCustomer().setStatus("Process of hard cosmetic");
		}

		message.setCode(Mc.cosmetic);
		hold(duration, message);
	}

	public void cosmetic(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.cosmetic:
				cosmetic(message);
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