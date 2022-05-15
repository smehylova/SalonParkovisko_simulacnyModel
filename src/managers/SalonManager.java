package managers;

import OSPABA.*;
import entities.Customer;
import simulation.*;
import agents.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="4"
public class SalonManager extends Manager
{
	public SalonManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="CosmeticAgent", id="27", type="Response"
	public void processGoToCosmetic(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.receptionAgent));
		message.setCode(Mc.goToPayment);
		request(message);
	}

	//meta! sender="ReceptionAgent", id="29", type="Response"
	public void processGoToPayment(MessageForm message)
	{
		myAgent().getStatTimeSalon().addSample(mySim().currentTime() - ((MyMessage) message).getCustomer().getStartTime());
		((MyMessage) message).getCustomer().setStatus("Away");

		MyMessage msg = (MyMessage) message;
		if (!msg.getCustomer().isHair() && !msg.getCustomer().isClean() && !msg.getCustomer().isConsmetic()) {
			myAgent().addEndCustomers();
		}
		//
		//KONIEC ZAKAZNIKA ALEBO IST ZMENIT MIESTO NA PARKOVISKU
		//
		if (msg.getCustomer().isDriver()) {
			message.setAddressee(mySim().findAgent(Id.bossAgent));
			message.setCode(Mc.leaveDriver);
			notice(message);
		}
	}

	//meta! sender="HairAgent", id="26", type="Response"
	public void processGoToHair(MessageForm message)
	{
		if (((MyMessage) message).getCustomer().isConsmetic()) {
			message.setAddressee(mySim().findAgent(Id.cosmeticAgent));
			message.setCode(Mc.goToCosmetic);
		} else {
			message.setAddressee(mySim().findAgent(Id.receptionAgent));
			message.setCode(Mc.goToPayment);
		}
		request(message);
	}

	//meta! sender="ReceptionAgent", id="25", type="Response"
	public void processGoToReception(MessageForm message)
	{
		if (((MyMessage) message).getCustomer().isRemoved()) {
			if (((MyMessage) message).getCustomer().isDriver()) {
				message.setAddressee(mySim().findAgent(Id.bossAgent));
				message.setCode(Mc.leaveDriver);
				notice(message);
			}
		} else {
			MyMessage msg = ((MyMessage) message);
			if (msg.getCustomer().isHair()) {
				message.setCode(Mc.goToHair);
				message.setAddressee(mySim().findAgent(Id.hairAgent));
			} else {
				message.setCode(Mc.goToCosmetic);
				message.setAddressee(mySim().findAgent(Id.cosmeticAgent));
			}
			request(message);
		}
	}

	//meta! sender="GoingToSalonProcess", id="46", type="Finish"
	public void processFinish(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.receptionAgent));
		message.setCode(Mc.goToReception);
		request(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="BossAgent", id="69", type="Notice"
	public void processServeCustomer(MessageForm message)
	{
		myAgent().addCustomers();
		Customer customer = ((MyMessage) message).getCustomer();
		customer.setStartTime(mySim().currentTime());
		double randHairCosmetic = myAgent().getHairCosmeticGenerator().sample();
		if (randHairCosmetic < 0.2) {
			customer.setHair(true);
		} else {
			if (randHairCosmetic < 0.35) {
				customer.setConsmetic(true);
			} else {
				customer.setHair(true);
				customer.setConsmetic(true);
			}
			double randClean = myAgent().getCleanGenerator().sample();
			if (randClean < 0.35) {
				customer.setClean(true);
			}
		}

		((MyMessage) message).getCustomer().setStatus("walking to salon");
		message.setAddressee(myAgent().findAssistant(Id.goingToSalonProcess));
		message.setCode(Mc.serving);
		startContinualAssistant(message);
	}

	//meta! sender="HairAgent", id="75", type="Notice"
	public void processSendQueueLengthHair(MessageForm message)
	{
		myAgent().setQueueLengthHair(((MyMessage) message).getQueueLength());
		((MyMessage) message).setQueueLength(((MyMessage) message).getQueueLength() + myAgent().getQueueLengthCosmetic());
		message.setAddressee(mySim().findAgent(Id.receptionAgent));
		message.setCode(Mc.sendQueueLength);
		notice(message);
	}

	//meta! sender="CosmeticAgent", id="74", type="Notice"
	public void processSendQueueLengthCosmetic(MessageForm message)
	{
		myAgent().setQueueLengthCosmetic(((MyMessage) message).getQueueLength());
		((MyMessage) message).setQueueLength(((MyMessage) message).getQueueLength() + myAgent().getQueueLengthHair());
		message.setAddressee(mySim().findAgent(Id.receptionAgent));
		message.setCode(Mc.sendQueueLength);
		notice(message);
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

		case Mc.sendQueueLengthCosmetic:
			processSendQueueLengthCosmetic(message);
		break;

		case Mc.sendQueueLengthHair:
			processSendQueueLengthHair(message);
		break;

		case Mc.goToCosmetic:
			processGoToCosmetic(message);
		break;

		case Mc.goToReception:
			processGoToReception(message);
		break;

		case Mc.goToPayment:
			processGoToPayment(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.serveCustomer:
			processServeCustomer(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public SalonAgent myAgent()
	{
		return (SalonAgent)super.myAgent();
	}

}