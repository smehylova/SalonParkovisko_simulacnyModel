

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import entities.Cosmetitian;
import entities.Customer;
import entities.HairStylist;
import entities.Receptionist;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import simulation.MySimulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

public class FormMain extends JFrame implements ISimDelegate/* implements ISimData*/ {
    private JLabel lCountReceptionists;
    private JLabel lCountHairStylists;
    private JLabel lCountCosmetitians;
    private JSpinner sCountReceptionists;
    private JSpinner sCountHairStylists;
    private JSpinner sCountCosmetitians;
    private JButton btnStartSimulation;
    private JTabbedPane tabbedPane1;
    private JLabel lQueueReception;
    private JLabel lQueueHair;
    private JLabel lQueueCosmetic;
    private JLabel lQueuePay;
    private JLabel lCountOccupiedR;
    private JLabel lCountOccupiedH;
    private JLabel lCountOccupiedC;
    private JPanel MainPanel;
    private JLabel lSimTime;
    private JLabel lAverageTimeInSalon;
    private JButton btnStopSimulation;
    private JLabel lAverageTimeQueueReception;
    private JLabel lCountCustomers;
    private JTextArea taMessages;
    private JSlider sliderSpeed;
    private JLabel lLengthQueueR;
    private JCheckBox cbSpeed;
    private JSpinner sReplications;
    private JLabel lReplicationCountCustomers;
    private JLabel lReplicationsTimeSalon;
    private JLabel lReplicationsTimeQueueReception;
    private JLabel lReplicationsLengthQueueReception;
    private JTable tableCustomers;
    private JLabel lActualReplication;
    private JLabel lReplicationsLengthQueueHair;
    private JLabel lReplicationsLengthQueueCosmetic;
    private JLabel lReplicationsLengthQueuePayment;
    private JButton btnPause;
    private JButton btnContinue;
    private JTable tableReceptionist;
    private JTable tableCosmetitian;
    private JTable tableHairStylist;
    private JLabel lAverageTimeQueueHair;
    private JLabel lAverageTimeQueueCosmetic;
    private JLabel lAverageTimeQueuePayment;
    private JLabel lLengthQueueH;
    private JLabel lLengthQueueC;
    private JLabel lLengthQueueP;
    private JLabel lReplicationsTimeQueueHair;
    private JLabel lReplicationsTimeQueueCosmetic;
    private JLabel lReplicationsTimeQueuePayment;
    private JLabel lReplicationOvertime;
    private JLabel lReplicationIntervalTimeSalon;
    private JPanel graphPanel;
    private JButton vykreslenieGrafuButton;
    private JLabel lReplicationLengthQueueReception17;
    private JLabel lReplicationLengthQueueHair17;
    private JLabel lReplicationLengthQueueCosmetic17;
    private JLabel lReplicationLengthQueuePayment17;
    private JLabel lReplicationUtilizationR;
    private JLabel lReplicationUtilizationH;
    private JLabel lReplicationUtilizationC;
    private JTable tableDrivers;
    private JTable tableCarPark;
    private JSpinner sCountParkRows;
    private JComboBox cbStrategy;
    private JLabel lSuccessDrivers;
    private JLabel lParkingSuccess;
    private JCheckBox cbAdvert;

    //private Salon salon;
    private MySimulation simSalon;
    //private ISimData self;
    private ISimDelegate self;
    DefaultTableModel modelTableCustomers;
    DefaultTableModel modelTableReceptionists;
    DefaultTableModel modelTableHairstylists;
    DefaultTableModel modelTableCosmetitians;
    DefaultTableModel modelTableDrivers;
    DefaultTableModel modelTableCarPark;

    //GRAF
    private static JFreeChart chart;
    private static final XYSeriesCollection dataset = new XYSeriesCollection();
    private static final XYSeries series = new XYSeries("Strategy");
    private boolean graph = false;


    public FormMain() {
        self = this;
        drawGraph();
        //drawHistogram();
        setContentPane(MainPanel);
        setTitle("Salon");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        //nastavenie prostredia
        sReplications.setValue(100000);
        sCountReceptionists.setValue(2);
        sCountHairStylists.setValue(10);
        sCountCosmetitians.setValue(8);

        sCountParkRows.setValue(2);

        //zakaznici
        createTables();

        btnStartSimulation.setEnabled(true);
        btnStopSimulation.setEnabled(false);
        btnPause.setEnabled(false);
        btnContinue.setEnabled(false);

        System.out.println(cbStrategy.getSelectedIndex());


        btnStartSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    public void run(){
                        btnStartSimulation.setEnabled(false);
                        btnStopSimulation.setEnabled(true);
                        btnPause.setEnabled(true);
                        btnContinue.setEnabled(false);

                        if (cbSpeed.isSelected()) {
                            cbSpeed.setEnabled(false);
                        }

                        //spustenie simulacie
                        createWorkers();

                        simSalon = new MySimulation();

                        simSalon.registerDelegate(self);
                        if (cbSpeed.isSelected()) {
                            simSalon.setSimSpeed(0, 0);
                            simSalon.setMaxSimSpeed();
                        } else {
                            simSalon.setSimSpeed(5*60, 1.0/(sliderSpeed.getValue()));
                        }
                        simSalon.onReplicationDidFinish(new Consumer<Simulation>() {
                            @Override
                            public void accept(Simulation simulation) {
                                modelTableReceptionists.setRowCount(0);
                                modelTableHairstylists.setRowCount(0);
                                modelTableCosmetitians.setRowCount(0);
                                modelTableCustomers.setRowCount(0);
                                modelTableDrivers.setRowCount(0);
                            }
                        });

                        simSalon.simulate((int) sReplications.getValue(), cbAdvert.isSelected(), cbStrategy.getSelectedIndex(), (int) sCountParkRows.getValue(), 8*60*60, (int) sCountReceptionists.getValue(), (int) sCountHairStylists.getValue(), (int) sCountCosmetitians.getValue());

                        btnStartSimulation.setEnabled(true);
                        btnStopSimulation.setEnabled(false);
                        btnPause.setEnabled(false);
                        btnContinue.setEnabled(false);

                        cbSpeed.setEnabled(true);
                    }
                };

                thread.start();
            }
        });
        btnStopSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simSalon.stopSimulation();
                btnStartSimulation.setEnabled(true);
                btnStopSimulation.setEnabled(false);
                btnPause.setEnabled(false);
                btnContinue.setEnabled(false);

                cbSpeed.setEnabled(true);
            }
        });
        sliderSpeed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                simSalon.setSimSpeed(5*60, 1.0/(sliderSpeed.getValue()));
            }
        });
        cbSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (simSalon != null) {
                    if (cbSpeed.isSelected()) {
                        simSalon.setSimSpeed(0, 0);
                        simSalon.setMaxSimSpeed();
                    } else {
                        simSalon.setSimSpeed(5*60, 1.0/(sliderSpeed.getValue()));
                    }
                    //if (cbSpeed.isSelected() && simSalon..isRunning()) {
                    //    cbSpeed.setEnabled(false);
                    //}
                }
            }
        });
        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simSalon.pauseSimulation();
                btnStartSimulation.setEnabled(false);
                btnStopSimulation.setEnabled(true);
                btnPause.setEnabled(false);
                btnContinue.setEnabled(true);
            }
        });
        btnContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simSalon.resumeSimulation();
                btnStartSimulation.setEnabled(false);
                btnStopSimulation.setEnabled(true);
                btnPause.setEnabled(true);
                btnContinue.setEnabled(false);
            }
        });
        vykreslenieGrafuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                series.clear();
                graph = true;

                //spustenie simulacie
                Thread thread = new Thread(){
                    public void run(){
                        for (int i = 1; i <= 10; i++) {
                            //spustenie simulacie
                            simSalon = new MySimulation();

                            simSalon.registerDelegate(self);
                            simSalon.setSimSpeed(0, 0);
                            simSalon.setMaxSimSpeed();

                            simSalon.simulate(1000, cbAdvert.isSelected(), cbStrategy.getSelectedIndex(), (int) sCountParkRows.getValue(), 8*60*60, (int) sCountReceptionists.getValue(), i, (int) sCountCosmetitians.getValue());

                            series.add(i, simSalon.getStatReplicationsLengthQueueReception().mean());
                        }
                        graph = false;
                    }
                };
                thread.start();

            }
        });
    }

    private void createTables() {
        modelTableCustomers = new DefaultTableModel();
        modelTableCustomers.addColumn("ID");
        modelTableCustomers.addColumn("Spôsob príchodu");
        modelTableCustomers.addColumn("Čas príchodu");
        modelTableCustomers.addColumn("Status");
        tableCustomers.setModel(modelTableCustomers);

        modelTableReceptionists = new DefaultTableModel();
        modelTableReceptionists.addColumn("ID");
        modelTableReceptionists.addColumn("Obsadenosť");
        modelTableReceptionists.addColumn("Utilizácia");
        tableReceptionist.setModel(modelTableReceptionists);

        modelTableHairstylists = new DefaultTableModel();
        modelTableHairstylists.addColumn("ID");
        modelTableHairstylists.addColumn("Obsadenosť");
        modelTableHairstylists.addColumn("Utilizácia");
        tableHairStylist.setModel(modelTableHairstylists);

        modelTableCosmetitians = new DefaultTableModel();
        modelTableCosmetitians.addColumn("ID");
        modelTableCosmetitians.addColumn("Obsadenosť");
        modelTableCosmetitians.addColumn("Utilizácia");
        tableCosmetitian.setModel(modelTableCosmetitians);

        modelTableDrivers = new DefaultTableModel();
        modelTableDrivers.addColumn("ID");
        modelTableDrivers.addColumn("Status");
        modelTableDrivers.addColumn("Miesto parkovania");
        modelTableDrivers.addColumn("Úspešnosť zaparkovania");
        tableDrivers.setModel(modelTableDrivers);

        modelTableCarPark = new DefaultTableModel();
        modelTableCarPark.addColumn("");
        modelTableCarPark.addColumn("15");
        modelTableCarPark.addColumn("14");
        modelTableCarPark.addColumn("13");
        modelTableCarPark.addColumn("12");
        modelTableCarPark.addColumn("11");
        modelTableCarPark.addColumn("10");
        modelTableCarPark.addColumn("9");
        modelTableCarPark.addColumn("8");
        modelTableCarPark.addColumn("7");
        modelTableCarPark.addColumn("6");
        modelTableCarPark.addColumn("5");
        modelTableCarPark.addColumn("4");
        modelTableCarPark.addColumn("3");
        modelTableCarPark.addColumn("2");
        modelTableCarPark.addColumn("1");
        Object[] data = new Object[3];
        data[0] = "A";
        modelTableCarPark.addRow(data);
        data = new Object[3];
        data[0] = "B";
        modelTableCarPark.addRow(data);
        data = new Object[3];
        data[0] = "C";
        modelTableCarPark.addRow(data);
        tableCarPark.setModel(modelTableCarPark);
    }

    public void createWorkers() {
        modelTableReceptionists.setRowCount(0);
        modelTableHairstylists.setRowCount(0);
        modelTableCosmetitians.setRowCount(0);

        for (int i = 0; i < (int) sCountReceptionists.getValue(); i++) {
            Object[] data = new Object[3];
            data[0] = i;
            data[1] = false;
            data[2] = 0;
            modelTableReceptionists.addRow(data);
        }

        for (int i = 0; i < (int) sCountHairStylists.getValue(); i++) {
            Object[] data = new Object[3];
            data[0] = i;
            data[1] = false;
            data[2] = 0;
            modelTableHairstylists.addRow(data);
        }

        for (int i = 0; i < (int) sCountCosmetitians.getValue(); i++) {
            Object[] data = new Object[3];
            data[0] = i;
            data[1] = false;
            data[2] = 0;
            modelTableCosmetitians.addRow(data);
        }
    }

    public void updateCarPark(int[][] carPark, LinkedList<Customer> drivers) {
        for (int i = 0; i < carPark.length; i++) {
            for (int j = 0; j < 15; j++) {
                if (carPark[i][j] == -1) {
                    modelTableCarPark.setValueAt("", i, 15 - j);
                } else {
                    modelTableCarPark.setValueAt(carPark[i][j], i, 15 - j);
                }
            }
        }

        for (int i = 0 ; i< drivers.size(); i++) {
            Customer driver = drivers.get(i);
            if (i >= modelTableDrivers.getRowCount()) {
                Object[] data = new Object[4];
                data[0] = driver.id();
                data[1] = driver.getParkingStatus();
                data[2] = driver.getParkingPlace();
                data[3] = driver.getParkingSuccess();
                modelTableDrivers.addRow(data);
            } else {
                modelTableDrivers.setValueAt(driver.getParkingStatus(), i, 1);
                modelTableDrivers.setValueAt(driver.getParkingPlace(), i, 2);
                modelTableDrivers.setValueAt(driver.getParkingSuccess(), i, 3);
            }
        }
    }

    public void updateCustomer(LinkedList<Customer> customers) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (i >= modelTableCustomers.getRowCount()) {
                Object[] data = new Object[4];
                data[0] = customer.id();
                if (customer.isDriver()) {
                    data[1] = "šofér";
                } else {
                    data[1] = "chodec";
                }
                data[2] = customer.getStartTime();
                data[3] = customer.getStatus();
                modelTableCustomers.addRow(data);
            } else {
                modelTableCustomers.setValueAt(customer.getStartTime(), i, 2);
                modelTableCustomers.setValueAt(customer.getStatus(), i, 3);
            }
        }

    }

    public String toTimeFormat(double seconds) {
        double second = seconds % 60;
        int time = (int) seconds / 60;
        int minute = time % 60;
        int hour = time / 60;
        if (minute == 0 && hour == 0) {
            return String.format("%.3f", second) + " s";
        } else if (hour == 0) {
            return minute + " min " + String.format("%.3f", second) + " s";
        } else {
            return hour + " hod " + minute + " min " + String.format("%.3f", second) + " s";
        }
    }

    public void drawGraph() {
        dataset.addSeries(series);
        chart = ChartFactory.createScatterPlot(
                "Závislosť priemernej dĺžky rady na recepcií od počtu kaderníčok",
                "Počet kaderníčok",
                "priemerná dĺžka radu na recepcií",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        graphPanel.add(chartPanel);
        graphPanel.validate();
        chartPanel.setVisible(true);
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {
        //System.out.println("simStateChanged");
        //System.out.println(simulation.currentTime());

        if (simState.name() == "replicationRunning") {
            refresh(simulation);
        }
    }

    @Override
    public void refresh(Simulation simulation) {
        if (!graph) {
            double time = simSalon.currentTime();
            double seconds = time % 60;
            int pomTime = (int) time / 60;
            int minutes = pomTime % 60;
            pomTime = pomTime / 60;
            int hour = (9 + pomTime) % 24;
            lSimTime.setText(hour + ":" + minutes + ":" + seconds);

            lQueueReception.setText(String.valueOf(simSalon.get_receptionAgent().getQueueReception().size()));
            lQueueHair.setText(String.valueOf(simSalon.get_hairAgent().getQueueHair().size()));
            lQueueCosmetic.setText(String.valueOf(simSalon.get_cosmeticAgent().getQueueCosmetic().size()));
            lQueuePay.setText(String.valueOf(simSalon.get_receptionAgent().getQueuePayment().size()));

            if (!cbSpeed.isSelected()) {
                ArrayList<Receptionist> receptionists = simSalon.get_receptionAgent().getReceptionists();
                int countReceptionists = 0;
                for (int i = 0; i < receptionists.size(); i++) {
                    Receptionist receptionist = receptionists.get(i);
                    if (receptionist.isOccupied()) {
                        countReceptionists++;
                    }
                    modelTableReceptionists.setValueAt(receptionist.id(), receptionist.id(), 0);
                    modelTableReceptionists.setValueAt(receptionist.isOccupied(), receptionist.id(), 1);
                    modelTableReceptionists.setValueAt((receptionist.getWork() / (double) simSalon.currentTime()) * 100 + "%", receptionist.id(), 2);
                }
                lCountOccupiedR.setText(String.valueOf(countReceptionists));

                ArrayList<HairStylist> hairStylists = simSalon.get_hairAgent().getHairStylists();
                int coutHairStylists = 0;
                for (int i = 0; i < hairStylists.size(); i++) {
                    HairStylist hairStylist = hairStylists.get(i);
                    if (hairStylist.isOccupied()) {
                        coutHairStylists++;
                    }
                    modelTableHairstylists.setValueAt(hairStylist.id(), hairStylist.id(), 0);
                    modelTableHairstylists.setValueAt(hairStylist.isOccupied(), hairStylist.id(), 1);
                    modelTableHairstylists.setValueAt((hairStylist.getWork() / (double) simSalon.currentTime()) * 100 + "%", hairStylist.id(), 2);
                }
                lCountOccupiedH.setText(String.valueOf(coutHairStylists));

                ArrayList<Cosmetitian> cosmetitians = simSalon.get_cosmeticAgent().getCosmetitians();
                int countCosmetitians = 0;
                for (int i = 0; i < cosmetitians.size(); i++) {
                    Cosmetitian cosmetitian = cosmetitians.get(i);
                    if (cosmetitian.isOccupied()) {
                        countCosmetitians++;
                    }
                    modelTableCosmetitians.setValueAt(cosmetitian.id(), cosmetitian.id(), 0);
                    modelTableCosmetitians.setValueAt(cosmetitian.isOccupied(), cosmetitian.id(), 1);
                    modelTableCosmetitians.setValueAt((cosmetitian.getWork() / (double) simSalon.currentTime()) * 100 + "%", cosmetitian.id(), 2);
                }
                lCountOccupiedC.setText(String.valueOf(countCosmetitians));

                updateCustomer(((MySimulation) simulation).environmentAgent().getCustomers());
                updateCarPark(((MySimulation) simulation).parkingAgent().getCarPark().getParkingPlaces(), ((MySimulation) simulation).parkingAgent().getDrivers());

            }

            lAverageTimeInSalon.setText(toTimeFormat(simSalon.get_salonAgent().getStatTimeSalon().mean()));
            //lCountCustomers.setText(String.valueOf(salon.getCountCustomers()));

            lAverageTimeQueueReception.setText(toTimeFormat(simSalon.get_receptionAgent().getStatQueueTimeReception().mean()));
            lAverageTimeQueueHair.setText(toTimeFormat(simSalon.get_hairAgent().getStatQueueTimeHair().mean()));
            lAverageTimeQueueCosmetic.setText(toTimeFormat(simSalon.get_cosmeticAgent().getStatQueueTimeCosmetic().mean()));
            lAverageTimeQueuePayment.setText(toTimeFormat(simSalon.get_receptionAgent().getStatQueueTimePayment().mean()));

            lLengthQueueR.setText(String.format("%.3f", simSalon.get_receptionAgent().getQueueReception().lengthStatistic().mean()));
            lLengthQueueH.setText(String.format("%.3f", simSalon.get_hairAgent().getQueueHair().lengthStatistic().mean()));
            lLengthQueueC.setText(String.format("%.3f", simSalon.get_cosmeticAgent().getQueueCosmetic().lengthStatistic().mean()));
            lLengthQueueP.setText(String.format("%.3f", simSalon.get_receptionAgent().getQueuePayment().lengthStatistic().mean()));

            ////////////////////
            ////REPLICATIONS////
            ////////////////////

            MySimulation sim = (MySimulation) simulation;
            lActualReplication.setText((simulation.currentReplication() + 1) + " z " + simulation.replicationCount());

            lReplicationCountCustomers.setText(String.format("%.3f", sim.getStatReplicationsCountCustomers().mean()));
            lReplicationsTimeSalon.setText(toTimeFormat(sim.getStatReplicationsTimeSalon().mean()));
            lReplicationOvertime.setText(toTimeFormat(sim.getStatReplicationsOvertime().mean()));

            lReplicationsTimeQueueReception.setText(toTimeFormat(sim.getStatReplicationsTimeQueueReception().mean()));
            lReplicationsTimeQueueHair.setText(toTimeFormat(sim.getStatReplicationsTimeQueueHair().mean()));
            lReplicationsTimeQueueCosmetic.setText(toTimeFormat(sim.getStatReplicationsTimeQueueCosmetic().mean()));
            lReplicationsTimeQueuePayment.setText(toTimeFormat(sim.getStatReplicationsTimeQueuePayment().mean()));

            lReplicationsLengthQueueReception.setText(String.format("%.3f", sim.getStatReplicationsLengthQueueReception().mean()));
            lReplicationsLengthQueueHair.setText(String.format("%.3f", sim.getStatReplicationsLengthQueueHair().mean()));
            lReplicationsLengthQueueCosmetic.setText(String.format("%.3f", sim.getStatReplicationsLengthQueueCosmetic().mean()));
            lReplicationsLengthQueuePayment.setText(String.format("%.5f", sim.getStatReplicationsLengthQueuePayment().mean()));

            lReplicationUtilizationR.setText(String.format("%.3f", sim.getStatReplicationsAverageUtilizationReceptionist().mean()));
            lReplicationUtilizationH.setText(String.format("%.3f", sim.getStatReplicationsAverageUtilizationHairstylist().mean()));
            lReplicationUtilizationC.setText(String.format("%.3f", sim.getStatReplicationsAverageUtilizationCosmetitian().mean()));

            if (sim.getStatReplicationsTimeSalon().sampleSize() > 1) {
                lReplicationIntervalTimeSalon.setText("<" + toTimeFormat(sim.getStatReplicationsTimeSalon().confidenceInterval_90()[0]) + ", " + toTimeFormat(sim.getStatReplicationsTimeSalon().confidenceInterval_90()[1]) + ">");
            }

            lSuccessDrivers.setText(String.format("%.3f", sim.getStatReplicationsPercentageSuccessDrivers().mean()));
            lParkingSuccess.setText(String.format("%.3f", sim.getStatReplicationsParkingSuccess().mean()));

            lReplicationLengthQueueReception17.setText(String.valueOf(sim.getStatReplicationsQueueLengthReception17().mean()));
            lReplicationLengthQueueHair17.setText(String.valueOf(sim.getStatReplicationsQueueLengthHair17().mean()));
            lReplicationLengthQueueCosmetic17.setText(String.valueOf(sim.getStatReplicationsQueueLengthCosmetic17().mean()));
            lReplicationLengthQueuePayment17.setText(String.valueOf(sim.getStatReplicationsQueueLengthPayment17().mean()));

        }
    }
}
