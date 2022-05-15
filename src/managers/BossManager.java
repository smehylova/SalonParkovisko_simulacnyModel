package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="1"
public class BossManager extends Manager
{
	public BossManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="ParkingAgent", id="15", type="Response"
	public void processParkCustomer(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.salonAgent));
		message.setCode(Mc.serveCustomer);
		request(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.initialization:
				processInitialization(message);
		}
	}

	public void processInitialization(MessageForm message) {
		message.setAddressee(mySim().findAgent(Id.environmentAgent));
		notice(message);
	}

	//meta! sender="EnvironmentAgent", id="70", type="Notice"
	public void processArrivalCustomer(MessageForm message)
	{
		if (((MyMessage)message).getCustomer().isDriver()) {
			message.setAddressee(mySim().findAgent(Id.parkingAgent));
			message.setCode(Mc.parkCustomer);
			request(message);
		} else {
			message.setAddressee(mySim().findAgent(Id.salonAgent));
			message.setCode(Mc.serveCustomer);
			notice(message);
		}
	}

	//meta! sender="SalonAgent", id="71", type="Notice"
	public void processLeaveDriver(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.parkingAgent));
		message.setCode(Mc.freePlace);
		notice(message);
	}

	//meta! sender="SalonIsOpenProcess", id="130", type="Finish"
	public void processFinish(MessageForm message)
	{
		((MySimulation) mySim()).getStatReplicationsQueueLengthReception17().addSample(((MySimulation) mySim()).get_receptionAgent().getQueueReception().lengthStatistic().mean());
		((MySimulation) mySim()).getStatReplicationsQueueLengthHair17().addSample(((MySimulation) mySim()).get_hairAgent().getQueueHair().lengthStatistic().mean());
		((MySimulation) mySim()).getStatReplicationsQueueLengthCosmetic17().addSample(((MySimulation) mySim()).get_cosmeticAgent().getQueueCosmetic().lengthStatistic().mean());
		((MySimulation) mySim()).getStatReplicationsQueueLengthPayment17().addSample(((MySimulation) mySim()).get_receptionAgent().getQueuePayment().lengthStatistic().mean());
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
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.leaveDriver:
			processLeaveDriver(message);
		break;

		case Mc.parkCustomer:
			processParkCustomer(message);
		break;

		case Mc.arrivalCustomer:
			processArrivalCustomer(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public BossAgent myAgent()
	{
		return (BossAgent)super.myAgent();
	}

}