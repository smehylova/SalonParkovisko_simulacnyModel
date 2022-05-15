package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import entities.Cosmetitian;
import entities.HairStylist;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.ArrayList;
//import instantAssistants.*;

//meta! id="21"
public class CosmeticAgent extends Agent
{
	private SimQueue<MyMessage> queueCosmetic;

	private Stat statQueueTimeCosmetic;

	private ArrayList<Cosmetitian> cosmetitians;

	public CosmeticAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		addOwnMessage(Mc.cleaning);
		addOwnMessage(Mc.cosmetic);
	}

	@Override
	public void prepareReplication()
	{
		queueCosmetic = new SimQueue<>(new WStat(mySim()));

		statQueueTimeCosmetic = new Stat();

		cosmetitians = new ArrayList<>(((MySimulation) mySim()).getCountCosmetitian());
		for (int i = 0; i < ((MySimulation) mySim()).getCountCosmetitian(); i++) {
			Cosmetitian cosmetitian = new Cosmetitian(i, mySim());
			cosmetitians.add(i, cosmetitian);
		}

		super.prepareReplication();
		// Setup component for the next replication
	}

	public SimQueue<MyMessage> getQueueCosmetic() {
		return queueCosmetic;
	}

	public void setQueueCosmetic(SimQueue<MyMessage> queueCosmetic) {
		this.queueCosmetic = queueCosmetic;
	}

	public Stat getStatQueueTimeCosmetic() {
		return statQueueTimeCosmetic;
	}

	public void setStatQueueTimeCosmetic(Stat statQueueTimeCosmetic) {
		this.statQueueTimeCosmetic = statQueueTimeCosmetic;
	}

	public ArrayList<Cosmetitian> getCosmetitians() {
		return cosmetitians;
	}

	public void setCosmetitians(ArrayList<Cosmetitian> cosmetitians) {
		this.cosmetitians = cosmetitians;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new CosmeticManager(Id.cosmeticManager, mySim(), this);
		new CleaningProcess(Id.cleaningProcess, mySim(), this);
		new CosmeticProcess(Id.cosmeticProcess, mySim(), this);
		addOwnMessage(Mc.goToCosmetic);
	}
	//meta! tag="end"
}