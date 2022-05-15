package managers;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="82"
public class ParkTransitionManager extends Manager
{
	public ParkTransitionManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="ParkingAgent", id="86", type="Request"
	public void processGoToNextQueue(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		if (msg.getParkActualQueue() == 0) {
			msg.setParkDuration(distanceToTimeKilometerHour(13+35+13, 20));
		} else if (msg.getParkActualQueue() == 1) {
			msg.setParkDuration(distanceToTimeKilometerHour(13+35+13+10, 20));
		} else if (msg.getParkActualQueue() == 2) {
			msg.setParkDuration(distanceToTimeKilometerHour(13+35+13+10+8, 20));
		}
		message.setAddressee(myAgent().findAssistant(Id.parkingProcess));
		((MyMessage) message).setActualMethod(message.code());
		startContinualAssistant(message);
	}

	//meta! sender="ParkingAgent", id="90", type="Request"
	public void processNextQueue(MessageForm message)
	{
		MyMessage msg = (MyMessage) message;
		if (msg.getParkActualQueue() == 0) {
			msg.setParkDuration(distanceToTimeKilometerHour(10, 20));
		} else if (msg.getParkActualQueue() == 1) {
			msg.setParkDuration(distanceToTimeKilometerHour(8, 20));
		}
		message.setAddressee(myAgent().findAssistant(Id.parkingProcess));
		((MyMessage) message).setActualMethod(message.code());
		startContinualAssistant(message);
	}

	public String returnRow(int row) {
		if (row == 0) {
			return "A";
		} else if (row == 1) {
			return "B";
		} else {
			return "C";
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="ParkingAgent", id="100", type="Request"
	public void processStartParking(MessageForm message)
	{
		((MyMessage) message).setParkDuration(distanceToTimeKilometerHour(48, 20));
		message.setAddressee(myAgent().findAssistant(Id.parkingProcess));
		((MyMessage) message).setActualMethod(message.code());
		startContinualAssistant(message);
	}

	//meta! sender="ParkingAgent", id="107", type="Request"
	public void processNextPlace(MessageForm message)
	{
		((MyMessage) message).setParkDuration(distanceToTimeKilometerHour(35.0 / 15, 12));
		message.setAddressee(myAgent().findAssistant(Id.parkingProcess));
		((MyMessage) message).setActualMethod(message.code());
		startContinualAssistant(message);
	}

	public double distanceToTimeMetersSecond(double distanceM, double speed) {
		return distanceM / speed;
	}

	public double distanceToTimeKilometerHour(double distanceM, double speed) {
		double newSpeed = (speed * 1000) / 3600;
		return distanceToTimeMetersSecond(distanceM, newSpeed);
	}

	//meta! sender="ParkingAgent", id="110", type="Request"
	public void processGoToStartPlace(MessageForm message)
	{
		double distance;
		if (((MyMessage) message).getOwnStartPlace() != -1) {
			distance = (35.0 / 15) * ((MyMessage) message).getOwnStartPlace();
		} else {
			distance = (35.0 / 15) * ((MyMessage) message).getParkStartPlace();
		}
		((MyMessage) message).setParkDuration(distanceToTimeKilometerHour(distance, 12));
		message.setAddressee(myAgent().findAssistant(Id.parkingProcess));
		((MyMessage) message).setActualMethod(message.code());
		startContinualAssistant(message);
	}

	//meta! sender="ParkingAgent", id="114", type="Request"
	public void processFromSalonToCarPark(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.fromSalonToCarParkProcess));
		message.setCode(Mc.fromSalonToCarPark);
		startContinualAssistant(message);
	}

	//meta! sender="FromSalonToCarParkProcess", id="116", type="Finish"
	public void processFinishFromSalonToCarParkProcess(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.parkingAgent));
		message.setCode(Mc.fromSalonToCarPark);
		response(message);
	}

	//meta! sender="ParkingProcess", id="119", type="Finish"
	public void processFinishParkingProcess(MessageForm message)
	{
		message.setCode(((MyMessage) message).getActualMethod());
		switch (message.code())
		{
			case Mc.goToStartPlace:
				if (((MyMessage) message).getOwnStartPlace() != -1) {
					((MyMessage) message).setParkActualPlace(((MyMessage) message).getParkActualPlace() + ((MyMessage) message).getOwnStartPlace());
				} else {
					((MyMessage) message).setParkActualPlace(((MyMessage) message).getParkActualPlace() + ((MyMessage) message).getParkStartPlace());
				}
				((MyMessage) message).setOwnStartPlace(-1);
				break;
			case Mc.goToNextQueue:
				((MyMessage) message).getCustomer().setParkingStatus("In row A");
				((MyMessage) message).setParkActualQueue(0);
				((MyMessage) message).setParkActualPlace(-1);
				break;
			case Mc.startParking:
				((MyMessage) message).getCustomer().setParkingStatus("Starting in row A");
				((MyMessage) message).setParkActualQueue(0);
				((MyMessage) message).setParkActualPlace(-1);
				break;
			case Mc.nextPlace:
				((MyMessage) message).getCustomer().setParkingStatus("On place " + ((MyMessage) message).getPlaceInCarPark(((MyMessage) message).getParkActualQueue(), ((MyMessage) message).getParkActualPlace() + 1));
				((MyMessage) message).setParkActualPlace(((MyMessage) message).getParkActualPlace() + 1);
				break;
			case Mc.nextQueue:
				((MyMessage) message).getCustomer().setParkingStatus("In row " + returnRow(((MyMessage) message).getParkActualQueue() + 1));
				((MyMessage) message).setParkActualQueue(((MyMessage) message).getParkActualQueue() + 1);
				((MyMessage) message).setParkActualPlace(-1);
				break;
		}
		message.setAddressee(mySim().findAgent(Id.parkingAgent));
		response(message);
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
		case Mc.nextQueue:
			processNextQueue(message);
		break;

		case Mc.startParking:
			processStartParking(message);
		break;

		case Mc.nextPlace:
			processNextPlace(message);
		break;

		case Mc.goToStartPlace:
			processGoToStartPlace(message);
		break;

		case Mc.goToNextQueue:
			processGoToNextQueue(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.fromSalonToCarParkProcess:
				processFinishFromSalonToCarParkProcess(message);
			break;

			case Id.parkingProcess:
				processFinishParkingProcess(message);
			break;
			}
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
	public ParkTransitionAgent myAgent()
	{
		return (ParkTransitionAgent)super.myAgent();
	}

}