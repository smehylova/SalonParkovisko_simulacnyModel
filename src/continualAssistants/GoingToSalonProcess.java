package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="45"
public class GoingToSalonProcess extends Process
{
	private UniformContinuousRNG walkSpeedGenerator;

	public GoingToSalonProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		walkSpeedGenerator = new UniformContinuousRNG(2.5 - 0.7, 2.5 + 0.7);
	}

	//meta! sender="SalonAgent", id="46", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.serving);
		double speed = walkSpeedGenerator.sample();
		((MyMessage) message).getCustomer().setGoingToSalonSpeed(speed);
		hold(distanceToTimeMetersSecond(((MyMessage) message).getCustomer().getGoingToSalonDistance(), speed), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.serving:
				assistantFinished(message);
				break;
		}
	}

	public double distanceToTimeMetersSecond(double distanceM, double speed) {
		return distanceM / speed;
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
	public SalonAgent myAgent()
	{
		return (SalonAgent)super.myAgent();
	}

}