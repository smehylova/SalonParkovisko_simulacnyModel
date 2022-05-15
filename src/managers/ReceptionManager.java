package managers;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import entities.Customer;
import entities.Receptionist;
import simulation.*;
import agents.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="19"
public class ReceptionManager extends Manager
{
	public ReceptionManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="SalonAgent", id="29", type="Request"
	public void processGoToPayment(MessageForm message)
	{
		MyMessage msg = ((MyMessage) message);
		msg.getCustomer().setStartQueueTime(mySim().currentTime());
		myAgent().getQueuePayment().enqueue(msg);
		msg.getCustomer().setStatus("In queue (payment)");

		serveCustomer(msg);
	}

	//meta! sender="SalonAgent", id="25", type="Request"
	public void processGoToReception(MessageForm message)
	{
		MyMessage msg = ((MyMessage) message);
		msg.getCustomer().setStartQueueTime(mySim().currentTime());
		myAgent().getQueueReception().enqueue(msg);
		msg.getCustomer().setStatus("In queue (reception)");

		serveCustomer(msg);
	}

	//meta! sender="PayingProcess", id="50", type="Finish"
	public void processFinishPayingProcess(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		msg.getEmployee().setOccupied(false);
		((Receptionist) msg.getEmployee()).setTypeOfWork('x');
		msg.getEmployee().addWord(mySim().currentTime() - msg.getEmployee().getStartWork());
		msg.setEmployee(null);

		msg.setAddressee(mySim().findAgent(Id.salonAgent));
		msg.setCode(Mc.goToPayment);
		response(msg);

		serveCustomer(msg);
	}

	//meta! sender="OrderingProcess", id="48", type="Finish"
	public void processFinishOrderingProcess(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		msg.getEmployee().setOccupied(false);
		((Receptionist) msg.getEmployee()).setTypeOfWork('x');
		msg.getEmployee().addWord(mySim().currentTime() - msg.getEmployee().getStartWork());
		msg.setEmployee(null);

		msg.setAddressee(mySim().findAgent(Id.salonAgent));
		msg.setCode(Mc.goToReception);
		response(msg);

		serveCustomer(msg);
	}

	public void serveCustomer(MyMessage message) {
		if (mySim().currentTime() > ((MySimulation) mySim()).getMaxTimeOfSalon()) {
			for (int j = 0; j < myAgent().getQueueReception().size(); j++) {
				MyMessage msg = myAgent().getQueueReception().dequeue();
				msg.getCustomer().setStatus("Removed!");

				msg.getCustomer().setRemoved(true);

				msg.setAddressee(mySim().findAgent(Id.salonAgent));
				response(msg);
			}
		}

		for (int i = 0; i < myAgent().getReceptionists().size(); i++) {
			if (!myAgent().getReceptionists().get(i).isOccupied()) {
				if (!myAgent().getQueuePayment().isEmpty()) {
					//platba
					onPayment(message);
					break;
				} else if (!myAgent().getQueueReception().isEmpty()) {
					//recepcia
					onReception(message);
					break;
				}
			}
		}
	}

	public void onReception(MessageForm message) {
		int countActualOrders = 0;
		for (int j = 0; j < myAgent().getReceptionists().size(); j++) {
			if (myAgent().getReceptionists().get(j).isOccupied() && myAgent().getReceptionists().get(j).getTypeOfWork() == 'r') {
				countActualOrders++;
			}
		}

		int count = myAgent().getCountCustomersInQueues() + countActualOrders;
		if (count < 11) {
			Receptionist actualReceptionist = null;
			double actualOrders = Double.MAX_VALUE;
			for (int i = 0; i < myAgent().getReceptionists().size(); i++) {
				if (!myAgent().getReceptionists().get(i).isOccupied() &&
						myAgent().getReceptionists().get(i).getWork() < actualOrders) {
					actualReceptionist = myAgent().getReceptionists().get(i);
					actualOrders = actualReceptionist.getWork();
				}
			}

			if (actualReceptionist != null) {
				actualReceptionist.setOccupied(true);
				actualReceptionist.setTypeOfWork('r');
				actualReceptionist.setStartWork(mySim().currentTime());
				message = myAgent().getQueueReception().dequeue();
				((MyMessage) message).getCustomer().endOfWaitingTimeQueue();
				myAgent().getStatQueueTimeReception().addSample(((MyMessage) message).getCustomer().getWaitingTime());

				((MyMessage) message).getCustomer().setStatus("Process of reception");

				((MyMessage) message).setEmployee(actualReceptionist);

				message.setAddressee(myAgent().findAssistant(Id.orderingProcess));
				startContinualAssistant(message);
			}
		}
	}

	public void onPayment(MessageForm message) {
		Receptionist actualReceptionist = null;
		double actualOrders = Double.MAX_VALUE;
		for (int i = 0; i < myAgent().getReceptionists().size(); i++) {
			if (!myAgent().getReceptionists().get(i).isOccupied() &&
					myAgent().getReceptionists().get(i).getWork() < actualOrders) {
				actualReceptionist = myAgent().getReceptionists().get(i);
				actualOrders = actualReceptionist.getWork();
			}
		}

		if (actualReceptionist != null) {
			actualReceptionist.setOccupied(true);
			actualReceptionist.setTypeOfWork('p');
			actualReceptionist.setStartWork(mySim().currentTime());
			MyMessage msg = myAgent().getQueuePayment().dequeue();
			msg.getCustomer().endOfWaitingTimeQueue();
			myAgent().getStatQueueTimePayment().addSample(msg.getCustomer().getWaitingTime());

			msg.getCustomer().setStatus("Process of payment");

			actualReceptionist.setOccupied(true);
			msg.setEmployee(actualReceptionist);

			msg.setAddressee(myAgent().findAssistant(Id.payingProcess));
			startContinualAssistant(msg);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="SalonAgent", id="76", type="Notice"
	public void processSendQueueLength(MessageForm message)
	{
		myAgent().setCountCustomersInQueues(((MyMessage) message).getQueueLength());
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
		case Mc.sendQueueLength:
			processSendQueueLength(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.orderingProcess:
				processFinishOrderingProcess(message);
			break;

			case Id.payingProcess:
				processFinishPayingProcess(message);
			break;
			}
		break;

		case Mc.goToPayment:
			processGoToPayment(message);
		break;

		case Mc.goToReception:
			processGoToReception(message);
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