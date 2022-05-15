package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import simulation.*;
import agents.*;

//meta! id="62"
public class ArrivalCarsScheduler extends Scheduler
{
	private ExponentialRNG expArrivalGenerator;

	public ArrivalCarsScheduler(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		double percentage;
		if (((MySimulation) mySim()).isAdvertCustomers20()) {
			percentage = 1.2;
		} else {
			percentage = 1;
		}
		expArrivalGenerator = new ExponentialRNG(3600/(8.0 * percentage));
	}

	//meta! sender="EnvironmentAgent", id="63", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.arrivingCar);
		hold(expArrivalGenerator.sample(), message);
	}

	public void processArrivingCar(MessageForm message) {
		double duration = expArrivalGenerator.sample();
		if (duration + mySim().currentTime() <= 28800) {
			MessageForm msg = message.createCopy();
			hold(duration, msg);
		}
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.arrivingCar:
				processArrivingCar(message);
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
	public EnvironmentAgent myAgent()
	{
		return (EnvironmentAgent)super.myAgent();
	}

}