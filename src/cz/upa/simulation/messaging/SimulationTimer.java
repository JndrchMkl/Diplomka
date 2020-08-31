package cz.upa.simulation.messaging;

import cz.upa.simulation.domain.Settings;
import cz.upa.simulation.gui.GraphSettings;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static cz.upa.simulation.domain.Settings.IS_SIMULATION_RUNNING;

public class SimulationTimer implements Runnable {

    @Override
    public void run() {
        TimerTask task = new TimerTask() {
            public void run() {
                IS_SIMULATION_RUNNING = false;
                Platform.runLater((new GraphSettings()));
                try {
                    URL bip = getClass().getResource("notofication.wav");
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bip);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                new Thread(new GraphSizePerTime()).start();
            }
        };
        Timer timer = new Timer("Timer");

        long delay = Settings.SIMULATION_DELAY_LENGTH;
        timer.schedule(task, delay);
    }
}
