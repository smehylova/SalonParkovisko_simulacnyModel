package managers;

import OSPABA.*;
import entities.Cosmetitian;
import simulation.*;
import agents.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="21"
public class CosmeticManager extends Manager
{
	public CosmeticManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="SalonAgent", id="27", type="Request"
	public void processGoToCosmetic(MessageForm message)
	{
		((MyMessage) message).getCustomer().setStartQueueTime(mySim().currentTime());
		myAgent().getQueueCosmetic().enqueue((MyMessage) message);

		MyMessage msg = (MyMessage) message.createCopy();
		msg.setQueueLength(myAgent().getQueueCosmetic().size());
		msg.setAddressee(mySim().findAgent(Id.salonAgent));
		msg.setCode(Mc.sendQueueLengthCosmetic);
		notice(msg);

		((MyMessage) message).getCustomer().setStatus("In queue (cosmetic)");

		serveCosmetic(message);
	}

	//meta! sender="CosmeticProcess", id="57", type="Finish"
	public void processFinishCosmeticProcess(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		msg.getCustomer().setConsmetic(false);
		msg.getEmployee().setOccupied(false);
		msg.getEmployee().addWord(mySim().currentTime() - msg.getEmployee().getStartWork());
		msg.setEmployee(null);

		msg.setAddressee(mySim().findAgent(Id.salonAgent));
		msg.setCode(Mc.goToCosmetic);
		response(msg);

		serveCosmetic(msg);
	}

	//meta! sender="CleaningProcess", id="55", type="Finish"
	public void processFinishCleaningProcess(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		msg.getCustomer().setClean(false);
		msg.getEmployee().setOccupied(false);
		msg.getEmployee().addWord(mySim().currentTime() - msg.getEmployee().getStartWork());
		msg.setEmployee(null);

		msg.getCustomer().setStartQueueTime(mySim().currentTime());
		msg.getCustomer().setStatus("In queue (cosmetic)");
		myAgent().getQueueCosmetic().enqueue(msg);

		MyMessage msgQueue = (MyMessage) message.createCopy();
		msgQueue.setQueueLength(myAgent().getQueueCosmetic().size());
		msgQueue.setAddressee(mySim().findAgent(Id.salonAgent));
		msgQueue.setCode(Mc.sendQueueLengthCosmetic);
		notice(msgQueue);

		serveCosmetic(msg);
	}

	public void serveCosmetic(MessageForm message) {
		Cosmetitian actualCosmetitian = null;
		double actualOrders = Double.MAX_VALUE;
		for (int i = 0; i < myAgent().getCosmetitians().size(); i++) {
			if (!myAgent().getCosmetitians().get(i).isOccupied() &&
					myAgent().getCosmetitians().get(i).getWork() < actualOrders) {
				actualCosmetitian = myAgent().getCosmetitians().get(i);
				actualOrders = actualCosmetitian.getWork();
			}
		}

		if (actualCosmetitian != null && !myAgent().getQueueCosmetic().isEmpty()) {
			actualCosmetitian.setOccupied(true);
			actualCosmetitian.setStartWork(mySim().currentTime());
			MyMessage msg = myAgent().getQueueCosmetic().dequeue();

			MyMessage msgQueue = (MyMessage) message.createCopy();
			msgQueue.setQueueLength(myAgent().getQueueCosmetic().size());
			msgQueue.setAddressee(mySim().findAgent(Id.salonAgent));
			msgQueue.setCode(Mc.sendQueueLengthCosmetic);
			notice(msgQueue);

			msg.getCustomer().endOfWaitingTimeQueue();
			myAgent().getStatQueueTimeCosmetic().addSample(msg.getCustomer().getWaitingTime());
			msg.setEmployee(actualCosmetitian);

			if (msg.getCustomer().isClean()) {
				msg.setAddressee(myAgent().findAssistant(Id.cleaningProcess));
				startContinualAssistant(msg);
			} else {
				msg.setAddressee(myAgent().findAssistant(Id.cosmeticProcess));
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
			switch (message.sender().id())
			{
			case Id.cosmeticProcess:
				processFinishCosmeticProcess(message);
			break;

			case Id.cleaningProcess:
				processFinishCleaningProcess(message);
			break;
			}
		break;

		case Mc.goToCosmetic:
			processGoToCosmetic(message);
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