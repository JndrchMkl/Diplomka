package cz.upa.simulation.messaging;

import cz.upa.simulation.domain.Settings;
import cz.upa.simulation.graph.GraphSizePerTime;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static cz.upa.simulation.domain.Settings.IS_SIMULATION_RUNNING;

public class SimulationTimer implements Runnable{

    @Override
    public void run() {
        TimerTask task = new TimerTask() {
            public void run() {
                IS_SIMULATION_RUNNING = false;
                new Thread(new GraphSizePerTime()).start();
            }
        };
        Timer timer = new Timer("Timer");

        long delay = Settings.SIMULATION_DELAY_LENGTH;
        timer.schedule(task, delay);
    }
}
