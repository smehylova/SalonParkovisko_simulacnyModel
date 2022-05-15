package agents;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import OSPStat.Stat;
import simulation.*;
import managers.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="4"
public class SalonAgent extends Agent
{
	private int countCustomers = 0;
	private int countEndCustomers = 0;

	private UniformContinuousRNG hairCosmeticGenerator;
	private UniformContinuousRNG cleanGenerator;

	private Stat statTimeSalon;

	private int queueLengthHair = 0;
	private int queueLengthCosmetic = 0;

	public SalonAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		addOwnMessage(Mc.serving);
	}

	@Override
	public void prepareReplication()
	{
		queueLengthHair = 0;
		queueLengthCosmetic = 0;

		countCustomers = 0;
		countEndCustomers = 0;

		hairCosmeticGenerator = new UniformContinuousRNG(0.0, 1.0);
		cleanGenerator = new UniformContinuousRNG(0.0, 1.0);
		statTimeSalon = new Stat();

		super.prepareReplication();
		// Setup component for the next replication
	}

	public void addCustomers() {
		this.countCustomers++;
	}

	public int getCountEndCustomers() {
		return countEndCustomers;
	}

	public void addEndCustomers() {
		this.countEndCustomers++;
	}

	public UniformContinuousRNG getHairCosmeticGenerator() {
		return hairCosmeticGenerator;
	}

	public UniformContinuousRNG getCleanGenerator() {
		return cleanGenerator;
	}

	public Stat getStatTimeSalon() {
		return statTimeSalon;
	}

	public int getQueueLengthHair() {
		return queueLengthHair;
	}

	public void setQueueLengthHair(int queueLengthHair) {
		this.queueLengthHair = queueLengthHair;
	}

	public int getQueueLengthCosmetic() {
		return queueLengthCosmetic;
	}

	public void setQueueLengthCosmetic(int queueLengthCosmetic) {
		this.queueLengthCosmetic = queueLengthCosmetic;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new SalonManager(Id.salonManager, mySim(), this);
		new GoingToSalonProcess(Id.goingToSalonProcess, mySim(), this);
		addOwnMessage(Mc.goToCosmetic);
		addOwnMessage(Mc.goToPayment);
		addOwnMessage(Mc.serveCustomer);
		addOwnMessage(Mc.goToHair);
		addOwnMessage(Mc.sendQueueLengthHair);
		addOwnMessage(Mc.goToReception);
		addOwnMessage(Mc.sendQueueLengthCosmetic);
	}
	//meta! tag="end"
}