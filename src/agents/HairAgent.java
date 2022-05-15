package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import entities.HairStylist;
import entities.Receptionist;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.ArrayList;
//import instantAssistants.*;

//meta! id="20"
public class HairAgent extends Agent
{
	private SimQueue<MyMessage> queueHair;

	private Stat statQueueTimeHair;

	private ArrayList<HairStylist> hairStylists;

	public HairAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		addOwnMessage(Mc.hair);
		addOwnMessage(Mc.cleaningHair);
	}

	@Override
	public void prepareReplication()
	{
		queueHair = new SimQueue<>(new WStat(mySim()));

		statQueueTimeHair = new Stat();

		hairStylists = new ArrayList<>(((MySimulation) mySim()).getCountHairStylist());
		for (int i = 0; i < ((MySimulation) mySim()).getCountHairStylist(); i++) {
			HairStylist hairStylist = new HairStylist(i, mySim());
			hairStylists.add(i, hairStylist);
		}

		super.prepareReplication();
		// Setup component for the next replication
	}

	public SimQueue<MyMessage> getQueueHair() {
		return queueHair;
	}

	public Stat getStatQueueTimeHair() {
		return statQueueTimeHair;
	}

	public ArrayList<HairStylist> getHairStylists() {
		return hairStylists;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new HairManager(Id.hairManager, mySim(), this);
		new CleanHairProcess(Id.cleanHairProcess, mySim(), this);
		new HairProcess(Id.hairProcess, mySim(), this);
		addOwnMessage(Mc.goToHair);
	}
	//meta! tag="end"
}