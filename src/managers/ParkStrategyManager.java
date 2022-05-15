package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
//import instantAssistants.*;

import java.util.ArrayList;

//meta! id="96"
public class ParkStrategyManager extends Manager
{
	public ParkStrategyManager(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="ParkingAgent", id="99", type="Request"
	public void processCreateStrategy(MessageForm message)
	{
		if (((MySimulation) mySim()).getParkStrategy() == Mc.strategyFirstToLastSix) {
			MyMessage msg = (MyMessage) message;
			msg.setParkStrategy(((MySimulation) mySim()).getParkStrategy());
			ArrayList<Integer> array = new ArrayList<>(((MySimulation) mySim()).getParkCountRows());
			ArrayList<Boolean> arrayBool = new ArrayList<>(((MySimulation) mySim()).getParkCountRows());
			for (int i = 0; i < ((MySimulation) mySim()).getParkCountRows(); i++) {
				array.add(i, i);
				arrayBool.add(i, false);
			}
			msg.setNoSearchedRows(((MySimulation) mySim()).getParkCountRows());
			msg.setResearchedRows(arrayBool);
			msg.setStrategy(array);
			msg.setParkStartPlace(6);
		} else if (((MySimulation) mySim()).getParkStrategy() == Mc.strategyFirstToLastOne) {
			MyMessage msg = (MyMessage) message;
			msg.setParkStrategy(((MySimulation) mySim()).getParkStrategy());
			ArrayList<Integer> array = new ArrayList<>(((MySimulation) mySim()).getParkCountRows());
			ArrayList<Boolean> arrayBool = new ArrayList<>(((MySimulation) mySim()).getParkCountRows());
			for (int i = 0; i < ((MySimulation) mySim()).getParkCountRows(); i++) {
				array.add(i, i);
				arrayBool.add(i, false);
			}
			msg.setNoSearchedRows(((MySimulation) mySim()).getParkCountRows());
			msg.setResearchedRows(arrayBool);
			msg.setStrategy(array);
			msg.setParkStartPlace(1);
		} else if (((MySimulation) mySim()).getParkStrategy() == Mc.strategyFirstToLastSixSkip) {
			MyMessage msg = (MyMessage) message;
			msg.setParkStrategy(((MySimulation) mySim()).getParkStrategy());
			ArrayList<Integer> array = new ArrayList<>(((MySimulation) mySim()).getParkCountRows());
			ArrayList<Boolean> arrayBool = new ArrayList<>(((MySimulation) mySim()).getParkCountRows());
			for (int i = 0; i < ((MySimulation) mySim()).getParkCountRows(); i++) {
				array.add(i, i);
				arrayBool.add(i, false);
			}
			msg.setNoSearchedRows(((MySimulation) mySim()).getParkCountRows());
			msg.setResearchedRows(arrayBool);
			msg.setStrategy(array);
			msg.setParkStartPlace(6);
			msg.setSkipRowsByFive(true);
		} else if (((MySimulation) mySim()).getParkStrategy() == Mc.strategyFirstToLastOneSkip) {
			MyMessage msg = (MyMessage) message;
			msg.setParkStrategy(((MySimulation) mySim()).getParkStrategy());
			ArrayList<Integer> array = new ArrayList<>(((MySimulation) mySim()).getParkCountRows());
			ArrayList<Boolean> arrayBool = new ArrayList<>(((MySimulation) mySim()).getParkCountRows());
			for (int i = 0; i < ((MySimulation) mySim()).getParkCountRows(); i++) {
				array.add(i, i);
				arrayBool.add(i, false);
			}
			msg.setNoSearchedRows(((MySimulation) mySim()).getParkCountRows());
			msg.setResearchedRows(arrayBool);
			msg.setStrategy(array);
			msg.setParkStartPlace(1);
			msg.setSkipRowsByFive(true);
		}

		message.setAddressee(mySim().findAgent(Id.parkingAgent));
		response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.createStrategy:
			processCreateStrategy(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public ParkStrategyAgent myAgent()
	{
		return (ParkStrategyAgent)super.myAgent();
	}

}