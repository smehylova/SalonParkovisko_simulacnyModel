package continualAssistants;

import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="51"
public class HairProcess extends Process
{
	private UniformContinuousRNG hairGenerator;
	private UniformDiscreteRNG easyHairGenerator;
	private EmpiricRNG hardHairGenerator;
	private EmpiricRNG weddingHairGenerator;

	public HairProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		hairGenerator = new UniformContinuousRNG(0.0, 1.0);
		easyHairGenerator = new UniformDiscreteRNG(10, 30);
		hardHairGenerator = new EmpiricRNG(
				new EmpiricPair(new UniformDiscreteRNG(30, 60), 0.4),
				new EmpiricPair(new UniformDiscreteRNG(61, 120), 0.6)
		);
		weddingHairGenerator = new EmpiricRNG(
				new EmpiricPair(new UniformDiscreteRNG(50, 60), 0.2),
				new EmpiricPair(new UniformDiscreteRNG(61, 100), 0.3),
				new EmpiricPair(new UniformDiscreteRNG(101, 150), 0.5)
		);
	}

	//meta! sender="HairAgent", id="52", type="Start"
	public void processStart(MessageForm message)
	{
		MyMessage msg = ((MyMessage) message);
		double randHair = hairGenerator.sample();
		int duration;
		if (randHair < 0.4) {
			duration = easyHairGenerator.sample() * 60;
			msg.getCustomer().setStatus("Process of easy hair");
		} else if (randHair < 0.8) {
			duration = hardHairGenerator.sample().intValue() * 60;
			msg.getCustomer().setStatus("Process of hard hair");
		} else {
			duration = weddingHairGenerator.sample().intValue() * 60;
			msg.getCustomer().setStatus("Process of wedding hair");
		}

		message.setCode(Mc.hair);
		hold(duration, message);
	}

	public void hair(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.hair:
				hair(message);
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