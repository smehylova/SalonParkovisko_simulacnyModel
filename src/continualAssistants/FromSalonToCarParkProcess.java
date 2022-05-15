package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="115"
public class FromSalonToCarParkProcess extends Process
{
	public FromSalonToCarParkProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="ParkTransitionAgent", id="116", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.fromSalonToCarPark);
		hold(distanceToTimeMetersSecond(((MyMessage) message).getCustomer().getGoingToSalonDistance(), ((MyMessage) message).getCustomer().getGoingToSalonSpeed()), message);
	}

	public void fromSalonToCarPark(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.fromSalonToCarPark:
				fromSalonToCarPark(message);
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
	public ParkTransitionAgent myAgent()
	{
		return (ParkTransitionAgent)super.myAgent();
	}

}