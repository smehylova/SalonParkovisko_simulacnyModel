package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import entities.Receptionist;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.ArrayList;
//import instantAssistants.*;

//meta! id="19"
public class ReceptionAgent extends Agent
{
	private int countCustomersInQueues = 0;

	private SimQueue<MyMessage> queueReception;
	private SimQueue<MyMessage> queuePayment;

	private Stat statQueueTimeReception;
	private Stat statQueueTimePayment;

	private ArrayList<Receptionist> receptionists;

	public ReceptionAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		addOwnMessage(Mc.ordering);
		addOwnMessage(Mc.paying);
	}

	@Override
	public void prepareReplication()
	{
		countCustomersInQueues = 0;

		queueReception = new SimQueue<>(new WStat(mySim()));
		queuePayment = new SimQueue<>(new WStat(mySim()));

		statQueueTimeReception = new Stat();
		statQueueTimePayment = new Stat();

		receptionists = new ArrayList<>(((MySimulation) mySim()).getCountReceptionist());
		for (int i = 0; i < ((MySimulation) mySim()).getCountReceptionist(); i++) {
			Receptionist receptionist = new Receptionist(i, mySim());
			receptionists.add(i, receptionist);
		}

		super.prepareReplication();
		// Setup component for the next replication
	}

	public SimQueue<MyMessage> getQueueReception() {
		return queueReception;
	}

	public SimQueue<MyMessage> getQueuePayment() {
		return queuePayment;
	}

	public ArrayList<Receptionist> getReceptionists() {
		return receptionists;
	}

	public Stat getStatQueueTimeReception() {
		return statQueueTimeReception;
	}

	public Stat getStatQueueTimePayment() {
		return statQueueTimePayment;
	}

	public int getCountCustomersInQueues() {
		return countCustomersInQueues;
	}

	public void setCountCustomersInQueues(int countCustomersInQueues) {
		this.countCustomersInQueues = countCustomersInQueues;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ReceptionManager(Id.receptionManager, mySim(), this);
		new OrderingProcess(Id.orderingProcess, mySim(), this);
		new PayingProcess(Id.payingProcess, mySim(), this);
		addOwnMessage(Mc.goToPayment);
		addOwnMessage(Mc.goToReception);
		addOwnMessage(Mc.sendQueueLength);
	}
	//meta! tag="end"
}