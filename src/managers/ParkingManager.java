package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
//import instantAssistants.*;

//meta! id="3"
public class ParkingManager extends Manager
{
	public ParkingManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="BossAgent", id="15", type="Request"
	public void processParkCustomer(MessageForm message)
	{
		myAgent().addCountDrivers();
		myAgent().getDrivers().add(((MyMessage) message).getCustomer());
		((MyMessage) message).getCustomer().setStatus("parking");
		((MyMessage) message).getCustomer().setParkingStatus("Going to car park");

		message.setCode(Mc.createStrategy);
		message.setAddressee(mySim().findAgent(Id.parkStrategyAgent));
		request(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="BossAgent", id="72", type="Notice"
	public void processFreePlace(MessageForm message)
	{
		((MyMessage) message).getCustomer().setParkingStatus("Going from salon to car park");
		message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
		message.setCode(Mc.fromSalonToCarPark);
		request(message);
	}

	//meta! sender="ParkTransitionAgent", id="86", type="Response"
	public void processGoToNextQueue(MessageForm message)
	{
		((MyMessage) message).getCustomer().addParkingSuccess(myAgent().getCarPark().getPenalValue());
		controlQueue(message);
	}

	//meta! sender="ParkTransitionAgent", id="90", type="Response"
	public void processNextQueue(MessageForm message)
	{
		controlQueue(message);
	}

	//meta! sender="ParkStrategyAgent", id="99", type="Response"
	public void processCreateStrategy(MessageForm message)
	{
		message.setCode(Mc.startParking);
		message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
		request(message);
	}

	//meta! sender="ParkTransitionAgent", id="100", type="Response"
	public void processStartParking(MessageForm message)
	{
		controlQueue(message);
	}

	public void deleteQueueFromStrategy(int row, MyMessage message) {
		for (int i = 0; i < message.getStrategy().size(); i++) {
			if (message.getStrategy().get(i) == row) {
				message.getStrategy().remove(i);
			}
		}
	}

	public void controlQueue(MessageForm message) {
		if (((MyMessage) message).getParkActualQueue() + 1 == myAgent().getCarPark().getCountRows()) {
			if (controlFivePlaces(((MyMessage) message)) == -1 && ((MyMessage) message).getNoSearchedRows() == myAgent().getCarPark().getCountRows()) {
				((MyMessage) message).setSkipRowsByFive(false);
			}
			((MyMessage) message).setOwnStartPlace(1);

			if (((MyMessage) message).getStrategy().get(0) == ((MyMessage) message).getParkActualQueue()) {
				((MyMessage) message).getStrategy().remove(0);
			}
			message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
			message.setCode(Mc.goToStartPlace);
			request(message);
		} else if (((MyMessage) message).getParkActualQueue() < ((MyMessage) message).getStrategy().get(0)) {
			int freePlace = controlFivePlaces((MyMessage) message);
			if (freePlace != -1) {
				((MyMessage) message).setOwnStartPlace(freePlace + 1);
				message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
				message.setCode(Mc.goToStartPlace);
				request(message);
			} else {
				message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
				message.setCode(Mc.nextQueue);
				request(message);
			}
		} else if (((MyMessage) message).getParkActualQueue() == ((MyMessage) message).getStrategy().get(0)) {
			int freePlace = controlFivePlaces((MyMessage) message);
			if (((MyMessage) message).isSkipRowsByFive() && freePlace == -1) {
				message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
				message.setCode(Mc.nextQueue);
				request(message);
			} else {
				((MyMessage) message).getStrategy().remove(0);
				message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
				message.setCode(Mc.goToStartPlace);
				request(message);
			}
		} else {
			int freePlace = controlFivePlaces((MyMessage) message);
			if (freePlace != -1) {
				((MyMessage) message).setOwnStartPlace(freePlace + 1);
				message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
				message.setCode(Mc.goToStartPlace);
				request(message);
			} else {
				message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
				message.setCode(Mc.nextQueue);
				request(message);
			}
		}
	}

	public int fromPlaceToSuccess(int row, int place) {
		int success = 0;
		success += (row * 15);
		success += (15 - place);
		return success;
	}

	public void parkingSuccess(MyMessage message) {
		int row = message.getCustomer().getParkingPlaceX();
		int place = message.getCustomer().getParkingPlaceY();
		message.getCustomer().addParkingSuccess(fromPlaceToSuccess(row, place));
		myAgent().getStatParkingSuccess().addSample(message.getCustomer().getParkingSuccess());

		double distance = 0.0;
		if (row == 2) {
			distance += 18;
		} else if (row == 1) {
			distance += 10;
		}
		distance += ((35 / 15.0) * (15 - place - 1));
		message.getCustomer().setGoingToSalonDistance(distance);

		message.getCustomer().setParkingPlace(message.getActualPlaceInCarPark());

		message.getCustomer().setParkingStatus("Parking success!");
		myAgent().addCountSuccessDrivers();

		message.setAddressee(mySim().findAgent(Id.bossAgent));
		message.setCode(Mc.parkCustomer);
		response(message);
	}

	public int controlFivePlaces(MyMessage message) {
		for (int i = 4; i >= 0; i--) {
			if (myAgent().getCarPark().getParkingPlaces()[message.getParkActualQueue()][i] == -1) {
				return i;
			}
		}
		return -1;
	}

	public void controlTransition(int actualPlace, int actualRow, MyMessage message) {
		if ((actualPlace < 14 && myAgent().getCarPark().getParkingPlaces()[actualRow][actualPlace] != -1) ||
				(actualPlace < 14 && myAgent().getCarPark().getParkingPlaces()[actualRow][actualPlace] == -1 && myAgent().getCarPark().getParkingPlaces()[actualRow][actualPlace + 1] == -1)) {
			message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
			message.setCode(Mc.nextPlace);
			request(message);
		} else if (actualPlace == 14 && myAgent().getCarPark().getParkingPlaces()[actualRow][actualPlace] != -1) {
			if (!message.getStrategy().isEmpty()) {
				message.setAddressee(mySim().findAgent(Id.parkTransitionAgent));
				message.setCode(Mc.goToNextQueue);
				request(message);
				if (!message.getResearchedRows().get(actualRow)) {
					message.getResearchedRows().set(actualRow, true);
					message.setNoSearchedRows(message.getNoSearchedRows() - 1);
				}
				deleteQueueFromStrategy(actualRow, message);
			} else {
				message.getCustomer().setStatus("parking failed");
				message.getCustomer().setParkingStatus("Parking failed!");
			}
		} else {
			message.getCustomer().setParkingPlaceX(actualRow);
			message.getCustomer().setParkingPlaceY(actualPlace);

			myAgent().getCarPark().getParkingPlaces()[actualRow][actualPlace] = message.getCustomer().id();

			parkingSuccess(message);
		}
	}

	//meta! sender="ParkTransitionAgent", id="107", type="Response"
	public void processNextPlace(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		controlTransition(msg.getParkActualPlace(), msg.getParkActualQueue(), msg);
	}

	//meta! sender="ParkTransitionAgent", id="110", type="Response"
	public void processGoToStartPlace(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		controlTransition(msg.getParkActualPlace(), msg.getParkActualQueue(), msg);
	}

	//meta! sender="ParkTransitionAgent", id="114", type="Response"
	public void processFromSalonToCarPark(MessageForm message)
	{
		((MyMessage) message).getCustomer().setParkingStatus("Away");
		MyMessage msg = (MyMessage) message;
		myAgent().getCarPark().getParkingPlaces()[msg.getCustomer().getParkingPlaceX()][msg.getCustomer().getParkingPlaceY()] = -1;
		msg.getCustomer().setParkingPlaceX(-1);
		msg.getCustomer().setParkingPlaceY(-1);
		msg.getCustomer().setParkingPlace("");
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
		case Mc.freePlace:
			processFreePlace(message);
		break;

		case Mc.nextQueue:
			processNextQueue(message);
		break;

		case Mc.startParking:
			processStartParking(message);
		break;

		case Mc.nextPlace:
			processNextPlace(message);
		break;

		case Mc.parkCustomer:
			processParkCustomer(message);
		break;

		case Mc.goToStartPlace:
			processGoToStartPlace(message);
		break;

		case Mc.createStrategy:
			processCreateStrategy(message);
		break;

		case Mc.goToNextQueue:
			processGoToNextQueue(message);
		break;

		case Mc.fromSalonToCarPark:
			processFromSalonToCarPark(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public ParkingAgent myAgent()
	{
		return (ParkingAgent)super.myAgent();
	}

}