package simulation;

import OSPABA.*;

public class Mc extends IdList
{
	//meta! userInfo="Generated code: do not modify", tag="begin"
	public static final int serveCustomer = 1017;
	public static final int arrivalCustomer = 1018;
	public static final int leaveDriver = 1019;
	public static final int freePlace = 1020;
	public static final int sendQueueLengthCosmetic = 1021;
	public static final int sendQueueLengthHair = 1022;
	public static final int initialization = 1001;
	public static final int sendQueueLength = 1023;
	public static final int parkCustomer = 1004;
	public static final int goToNextQueue = 1025;
	public static final int goToReception = 1008;
	public static final int goToHair = 1009;
	public static final int nextQueue = 1029;
	public static final int goToCosmetic = 1010;
	public static final int goToPayment = 1012;
	public static final int createStrategy = 1030;
	public static final int startParking = 1031;
	public static final int nextPlace = 1032;
	public static final int goToStartPlace = 1033;
	public static final int fromSalonToCarPark = 1034;
	//meta! tag="end"

	// 1..1000 range reserved for user
	public static final int strategyFirstToLastSix = 0;
	public static final int strategyFirstToLastOne = 1;
	public static final int strategyFirstToLastSixSkip = 2;
	public static final int strategyFirstToLastOneSkip = 3;

	public static final int arrivingCar = 11;
	public static final int arrivingPerson = 12;
	public static final int parking = 13;
	public static final int serving = 14;
	public static final int ordering = 15;
	public static final int paying = 16;
	public static final int hair = 17;
	public static final int cleaning = 18;
	public static final int cosmetic = 19;
	public static final int closeSalon = 20;
	public static final int cleaningHair = 21;

}