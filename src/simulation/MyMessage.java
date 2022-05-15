package simulation;

import OSPABA.*;
import entities.Customer;
import entities.Employee;

import java.util.ArrayList;

public class MyMessage extends MessageForm
{
	private Customer customer;
	private Employee employee = null;
	private int queueLength;

	private ArrayList<Integer> strategy = null;
	private ArrayList<Boolean> researchedRows = null;
	private int noSearchedRows = -1;
	private int parkStrategy = Mc.strategyFirstToLastSix;
	private boolean skipRowsByFive = false;

	private int parkStartPlace = -1;
	private double parkDuration = -1;
	private int parkActualQueue = -1;
	private int parkActualPlace = -1;

	private int ownStartPlace = -1;

	private int actualMethod;

	public MyMessage(Simulation sim)
	{
		super(sim);
	}

	public MyMessage(MyMessage original)
	{
		super(original);
		// copy() is called in superclass
	}

	public String getActualPlaceInCarPark() {
		return getPlaceInCarPark(customer.getParkingPlaceX(), customer.getParkingPlaceY());
	}

	public String getPlaceInCarPark(int row, int place) {
		if (row != -1) {
			String placeF;
			if (row == 0) {
				placeF = "A";
			} else if (row == 1) {
				placeF = "B";
			} else {
				placeF = "C";
			}
			placeF += (place + 1);
			return placeF;
		}
		return "";
	}

	@Override
	public MessageForm createCopy()
	{
		return new MyMessage(this);
	}

	@Override
	protected void copy(MessageForm message)
	{
		super.copy(message);
		MyMessage original = (MyMessage)message;
		// Copy attributes
		this.customer = original.getCustomer();
		this.employee = original.getEmployee();
		this.queueLength = original.getQueueLength();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public int getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;
	}

	public ArrayList<Integer> getStrategy() {
		return strategy;
	}

	public void setStrategy(ArrayList<Integer> strategy) {
		this.strategy = strategy;
	}

	public void setParkStrategy(int parkStrategy) {
		this.parkStrategy = parkStrategy;
	}

	public int getParkActualQueue() {
		return parkActualQueue;
	}

	public void setParkActualQueue(int parkActualQueue) {
		this.parkActualQueue = parkActualQueue;
	}

	public int getParkActualPlace() {
		return parkActualPlace;
	}

	public void setParkActualPlace(int parkActualPlace) {
		this.parkActualPlace = parkActualPlace;
	}

	public double getParkDuration() {
		return parkDuration;
	}

	public void setParkDuration(double parkDistance) {
		this.parkDuration = parkDistance;
	}

	public int getParkStartPlace() {
		return parkStartPlace;
	}

	public void setParkStartPlace(int parkStartPlace) {
		this.parkStartPlace = parkStartPlace;
	}

	public int getActualMethod() {
		return actualMethod;
	}

	public void setActualMethod(int actualMethod) {
		this.actualMethod = actualMethod;
	}

	public int getOwnStartPlace() {
		return ownStartPlace;
	}

	public void setOwnStartPlace(int ownStartPlace) {
		this.ownStartPlace = ownStartPlace;
	}

	public ArrayList<Boolean> getResearchedRows() {
		return researchedRows;
	}

	public void setResearchedRows(ArrayList<Boolean> researchedRows) {
		this.researchedRows = researchedRows;
	}

	public int getNoSearchedRows() {
		return noSearchedRows;
	}

	public void setNoSearchedRows(int noSearchedRows) {
		this.noSearchedRows = noSearchedRows;
	}

	public boolean isSkipRowsByFive() {
		return skipRowsByFive;
	}

	public void setSkipRowsByFive(boolean skipRowsByFive) {
		this.skipRowsByFive = skipRowsByFive;
	}
}