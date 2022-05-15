package simulation;

import OSPABA.*;
import OSPStat.Stat;
import agents.*;

public class MySimulation extends Simulation
{
	private double maxTimeOfSalon = 0;

	private int countReceptionist;
	private int countCosmetitian;
	private int countHairStylist;

	private int parkStrategy = Mc.strategyFirstToLastSix;
	private int parkCountRows = 3;

	private boolean advertCustomers20 = false;

	private Stat statReplicationsCountCustomers;
	private Stat statReplicationsTimeSalon;
	private Stat statReplicationsOvertime;

	private Stat statReplicationsTimeQueueReception;
	private Stat statReplicationsTimeQueueHair;
	private Stat statReplicationsTimeQueueCosmetic;
	private Stat statReplicationsTimeQueuePayment;

	private Stat statReplicationsLengthQueueReception;
	private Stat statReplicationsLengthQueueHair;
	private Stat statReplicationsLengthQueueCosmetic;
	private Stat statReplicationsLengthQueuePayment;

	private Stat statReplicationsAverageUtilizationReceptionist;
	private Stat statReplicationsAverageUtilizationHairstylist;
	private Stat statReplicationsAverageUtilizationCosmetitian;

	private Stat statReplicationsParkingSuccess;
	private Stat statReplicationsPercentageSuccessDrivers;

	//statistiky do 17
	private double statQueueLengthReception17;
	private double statQueueLengthHair17;
	private double statQueueLengthCosmetic17;
	private double statQueueLengthPayment17;

	private Stat statReplicationsQueueLengthReception17;
	private Stat statReplicationsQueueLengthHair17;
	private Stat statReplicationsQueueLengthCosmetic17;
	private Stat statReplicationsQueueLengthPayment17;

	public MySimulation()
	{
		init();
	}

	public void simulate(int countReplications, boolean advert, int strategy, int countRows, double maxTime, int countReceptionists, int countHairStylists, int countCosmetitians) {
		this.countReceptionist = countReceptionists;
		this.countCosmetitian = countCosmetitians;
		this.countHairStylist = countHairStylists;

		this.parkCountRows = countRows;
		this.parkStrategy = strategy;
		this.advertCustomers20 = advert;

		this.maxTimeOfSalon = maxTime;

		simulate(countReplications);
	}

	@Override
	public void prepareSimulation()
	{
		statReplicationsCountCustomers = new Stat();
		statReplicationsTimeSalon = new Stat();
		statReplicationsOvertime = new Stat();

		statReplicationsTimeQueueReception = new Stat();
		statReplicationsTimeQueueHair = new Stat();
		statReplicationsTimeQueueCosmetic = new Stat();
		statReplicationsTimeQueuePayment = new Stat();

		statReplicationsLengthQueueReception = new Stat();
		statReplicationsLengthQueueHair = new Stat();
		statReplicationsLengthQueueCosmetic = new Stat();
		statReplicationsLengthQueuePayment = new Stat();

		statReplicationsParkingSuccess = new Stat();
		statReplicationsPercentageSuccessDrivers = new Stat();

		statReplicationsAverageUtilizationReceptionist = new Stat();
		statReplicationsAverageUtilizationHairstylist = new Stat();
		statReplicationsAverageUtilizationCosmetitian = new Stat();

		statReplicationsQueueLengthReception17 = new Stat();
		statReplicationsQueueLengthHair17 = new Stat();
		statReplicationsQueueLengthCosmetic17 = new Stat();
		statReplicationsQueueLengthPayment17 = new Stat();

		super.prepareSimulation();
		// Create global statistcis
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...

		bossAgent().startSimulation();
	}

	@Override
	public void replicationFinished()
	{
		statReplicationsCountCustomers.addSample(_salonAgent.getCountEndCustomers());
		statReplicationsTimeSalon.addSample(_salonAgent.getStatTimeSalon().mean());
		statReplicationsOvertime.addSample(currentTime() - maxTimeOfSalon);

		statReplicationsTimeQueueReception.addSample(_receptionAgent.getStatQueueTimeReception().mean());
		statReplicationsTimeQueueHair.addSample(_hairAgent.getStatQueueTimeHair().mean());
		statReplicationsTimeQueueCosmetic.addSample(_cosmeticAgent.getStatQueueTimeCosmetic().mean());
		statReplicationsTimeQueuePayment.addSample(_receptionAgent.getStatQueueTimePayment().mean());

		statReplicationsLengthQueueReception.addSample(_receptionAgent.getQueueReception().lengthStatistic().mean());
		statReplicationsLengthQueueHair.addSample(_hairAgent.getQueueHair().lengthStatistic().mean());
		statReplicationsLengthQueueCosmetic.addSample(_cosmeticAgent.getQueueCosmetic().lengthStatistic().mean());
		statReplicationsLengthQueuePayment.addSample(_receptionAgent.getQueuePayment().lengthStatistic().mean());

		statReplicationsParkingSuccess.addSample(_parkingAgent.getStatParkingSuccess().mean());
		statReplicationsPercentageSuccessDrivers.addSample(((double) _parkingAgent.getCountSuccessDrivers() / _parkingAgent.getCountDrivers()) * 100.0);

		Stat pomStat = new Stat();
		for (int i = 0; i < _receptionAgent.getReceptionists().size(); i++) {
			pomStat.addSample(_receptionAgent.getReceptionists().get(i).getUtilization());
		}
		statReplicationsAverageUtilizationReceptionist.addSample(pomStat.mean());
		pomStat.clear();
		for (int i = 0; i < _hairAgent.getHairStylists().size(); i++) {
			pomStat.addSample(_hairAgent.getHairStylists().get(i).getUtilization());
		}
		statReplicationsAverageUtilizationHairstylist.addSample(pomStat.mean());
		pomStat.clear();
		for (int i = 0; i < _cosmeticAgent.getCosmetitians().size(); i++) {
			pomStat.addSample(_cosmeticAgent.getCosmetitians().get(i).getUtilization());
		}
		statReplicationsAverageUtilizationCosmetitian.addSample(pomStat.mean());

		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
	}

	@Override
	public void simulationFinished()
	{
		// Dysplay simulation results
		super.simulationFinished();
	}

	public double getMaxTimeOfSalon() {
		return maxTimeOfSalon;
	}

	public int getCountReceptionist() {
		return countReceptionist;
	}

	public int getCountCosmetitian() {
		return countCosmetitian;
	}

	public int getCountHairStylist() {
		return countHairStylist;
	}

	public Stat getStatReplicationsCountCustomers() {
		return statReplicationsCountCustomers;
	}

	public Stat getStatReplicationsTimeSalon() {
		return statReplicationsTimeSalon;
	}

	public Stat getStatReplicationsOvertime() {
		return statReplicationsOvertime;
	}

	public Stat getStatReplicationsTimeQueueReception() {
		return statReplicationsTimeQueueReception;
	}

	public Stat getStatReplicationsTimeQueueHair() {
		return statReplicationsTimeQueueHair;
	}

	public Stat getStatReplicationsTimeQueueCosmetic() {
		return statReplicationsTimeQueueCosmetic;
	}

	public Stat getStatReplicationsTimeQueuePayment() {
		return statReplicationsTimeQueuePayment;
	}

	public Stat getStatReplicationsLengthQueueReception() {
		return statReplicationsLengthQueueReception;
	}

	public Stat getStatReplicationsLengthQueueHair() {
		return statReplicationsLengthQueueHair;
	}

	public Stat getStatReplicationsLengthQueueCosmetic() {
		return statReplicationsLengthQueueCosmetic;
	}

	public Stat getStatReplicationsLengthQueuePayment() {
		return statReplicationsLengthQueuePayment;
	}

	public Stat getStatReplicationsAverageUtilizationReceptionist() {
		return statReplicationsAverageUtilizationReceptionist;
	}

	public Stat getStatReplicationsAverageUtilizationHairstylist() {
		return statReplicationsAverageUtilizationHairstylist;
	}

	public Stat getStatReplicationsAverageUtilizationCosmetitian() {
		return statReplicationsAverageUtilizationCosmetitian;
	}

	public Stat getStatReplicationsQueueLengthReception17() {
		return statReplicationsQueueLengthReception17;
	}

	public Stat getStatReplicationsQueueLengthHair17() {
		return statReplicationsQueueLengthHair17;
	}

	public Stat getStatReplicationsQueueLengthCosmetic17() {
		return statReplicationsQueueLengthCosmetic17;
	}

	public Stat getStatReplicationsQueueLengthPayment17() {
		return statReplicationsQueueLengthPayment17;
	}

	public SalonAgent get_salonAgent() {
		return _salonAgent;
	}

	public ReceptionAgent get_receptionAgent() {
		return _receptionAgent;
	}

	public HairAgent get_hairAgent() {
		return _hairAgent;
	}

	public CosmeticAgent get_cosmeticAgent() {
		return _cosmeticAgent;
	}

	public int getParkStrategy() {
		return parkStrategy;
	}

	public Stat getStatReplicationsParkingSuccess() {
		return statReplicationsParkingSuccess;
	}

	public int getParkCountRows() {
		return parkCountRows;
	}

	public Stat getStatReplicationsPercentageSuccessDrivers() {
		return statReplicationsPercentageSuccessDrivers;
	}

	public boolean isAdvertCustomers20() {
		return advertCustomers20;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setBossAgent(new BossAgent(Id.bossAgent, this, null));
		setEnvironmentAgent(new EnvironmentAgent(Id.environmentAgent, this, bossAgent()));
		setParkingAgent(new ParkingAgent(Id.parkingAgent, this, bossAgent()));
		setSalonAgent(new SalonAgent(Id.salonAgent, this, bossAgent()));
		setParkTransitionAgent(new ParkTransitionAgent(Id.parkTransitionAgent, this, parkingAgent()));
		setReceptionAgent(new ReceptionAgent(Id.receptionAgent, this, salonAgent()));
		setHairAgent(new HairAgent(Id.hairAgent, this, salonAgent()));
		setCosmeticAgent(new CosmeticAgent(Id.cosmeticAgent, this, salonAgent()));
		setParkStrategyAgent(new ParkStrategyAgent(Id.parkStrategyAgent, this, parkingAgent()));
	}

	private BossAgent _bossAgent;

public BossAgent bossAgent()
	{ return _bossAgent; }

	public void setBossAgent(BossAgent bossAgent)
	{_bossAgent = bossAgent; }

	private EnvironmentAgent _environmentAgent;

public EnvironmentAgent environmentAgent()
	{ return _environmentAgent; }

	public void setEnvironmentAgent(EnvironmentAgent environmentAgent)
	{_environmentAgent = environmentAgent; }

	private ParkingAgent _parkingAgent;

public ParkingAgent parkingAgent()
	{ return _parkingAgent; }

	public void setParkingAgent(ParkingAgent parkingAgent)
	{_parkingAgent = parkingAgent; }

	private SalonAgent _salonAgent;

public SalonAgent salonAgent()
	{ return _salonAgent; }

	public void setSalonAgent(SalonAgent salonAgent)
	{_salonAgent = salonAgent; }

	private ParkTransitionAgent _parkTransitionAgent;

public ParkTransitionAgent parkTransitionAgent()
	{ return _parkTransitionAgent; }

	public void setParkTransitionAgent(ParkTransitionAgent parkTransitionAgent)
	{_parkTransitionAgent = parkTransitionAgent; }

	private ReceptionAgent _receptionAgent;

public ReceptionAgent receptionAgent()
	{ return _receptionAgent; }

	public void setReceptionAgent(ReceptionAgent receptionAgent)
	{_receptionAgent = receptionAgent; }

	private HairAgent _hairAgent;

public HairAgent hairAgent()
	{ return _hairAgent; }

	public void setHairAgent(HairAgent hairAgent)
	{_hairAgent = hairAgent; }

	private CosmeticAgent _cosmeticAgent;

public CosmeticAgent cosmeticAgent()
	{ return _cosmeticAgent; }

	public void setCosmeticAgent(CosmeticAgent cosmeticAgent)
	{_cosmeticAgent = cosmeticAgent; }

	private ParkStrategyAgent _parkStrategyAgent;

public ParkStrategyAgent parkStrategyAgent()
	{ return _parkStrategyAgent; }

	public void setParkStrategyAgent(ParkStrategyAgent parkStrategyAgent)
	{_parkStrategyAgent = parkStrategyAgent; }
	//meta! tag="end"
}