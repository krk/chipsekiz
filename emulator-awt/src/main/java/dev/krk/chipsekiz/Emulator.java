package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IInterpreter;

import java.util.Timer;
import java.util.TimerTask;


public class Emulator {
    private IInterpreter interpreter;
    private volatile boolean isStopping;
    private volatile int actualFrequency;
    private double tickBudgetNs;
    private boolean paused;

    public Emulator(IInterpreter interpreter) {
        this.interpreter = interpreter;
        this.isStopping = false;
        this.actualFrequency = 0;
        this.setFrequency(5000);
    }

    public void setFrequency(int frequency) {
        this.tickBudgetNs = 1e9 / frequency;
    }

    public int getActualFrequency() {
        return actualFrequency;
    }

    public void stop() {
        isStopping = true;
    }

    public void run() {
        final int[] cycles = {0};

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                actualFrequency = cycles[0];
                cycles[0] = 0;
            }
        }, 0, 1000);

        double done = 0;
        long prev = System.nanoTime();
        while (!isStopping) {
            if (paused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            long now = System.nanoTime();
            long diff = now - prev;
            done += diff / tickBudgetNs;
            prev = now;

            if (done >= 1) {
                interpreter.tick();
                cycles[0]++;
                done = 0;
            }
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void setInterpreter(IInterpreter interpreter) {
        this.interpreter = interpreter;
    }
}
