package managers;

import OSPABA.*;
import entities.HairStylist;
import simulation.*;
import agents.*;
//import instantAssistants.*;

//meta! id="20"
public class HairManager extends Manager
{
	public HairManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="SalonAgent", id="26", type="Request"
	public void processGoToHair(MessageForm message)
	{
		((MyMessage) message).getCustomer().setStartQueueTime(mySim().currentTime());
		myAgent().getQueueHair().enqueue((MyMessage) message);

		MyMessage msg = (MyMessage) message.createCopy();
		msg.setQueueLength(myAgent().getQueueHair().size());
		msg.setAddressee(mySim().findAgent(Id.salonAgent));
		msg.setCode(Mc.sendQueueLengthHair);
		notice(msg);

		((MyMessage) message).getCustomer().setStatus("In queue (hair)");

		serveHair(message);
	}

	//meta! sender="HairProcess", id="52", type="Finish"
	public void processFinishHairProcess(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		msg.getCustomer().setHair(false);
		msg.getEmployee().setOccupied(false);
		msg.getEmployee().addWord(mySim().currentTime() - msg.getEmployee().getStartWork());
		msg.setEmployee(null);

		msg.setAddressee(mySim().findAgent(Id.salonAgent));
		msg.setCode(Mc.goToHair);
		response(msg);

		serveHair(msg);
	}

	public void serveHair(MessageForm message) {
		if (!myAgent().getQueueHair().isEmpty()) {
			HairStylist actualHairStaylist = null;
			double actualOrders = Double.MAX_VALUE;
			for (int i = 0; i < myAgent().getHairStylists().size(); i++) {
				if (!myAgent().getHairStylists().get(i).isOccupied() &&
						myAgent().getHairStylists().get(i).getWork() < actualOrders) {
					actualHairStaylist = myAgent().getHairStylists().get(i);
					actualOrders = actualHairStaylist.getWork();
				}
			}

			if (actualHairStaylist != null) {
				actualHairStaylist.setOccupied(true);
				actualHairStaylist.setStartWork(mySim().currentTime());
				MyMessage msg = myAgent().getQueueHair().dequeue();

				MyMessage msgQueue = (MyMessage) message.createCopy();
				msgQueue.setQueueLength(myAgent().getQueueHair().size());
				msgQueue.setAddressee(mySim().findAgent(Id.salonAgent));
				msgQueue.setCode(Mc.sendQueueLengthHair);
				notice(msgQueue);

				msg.getCustomer().endOfWaitingTimeQueue();
				myAgent().getStatQueueTimeHair().addSample(msg.getCustomer().getWaitingTime());
				msg.setEmployee(actualHairStaylist);

				msg.setAddressee(myAgent().findAssistant(Id.cleanHairProcess));
				startContinualAssistant(msg);
			}
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="CleanHairProcess", id="139", type="Finish"
	public void processFinishCleanHairProcess(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.hairProcess));
		startContinualAssistant(message);
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
		case Mc.goToHair:
			processGoToHair(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.hairProcess:
				processFinishHairProcess(message);
			break;

			case Id.cleanHairProcess:
				processFinishCleanHairProcess(message);
			break;
			}
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