package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IHal;
import dev.krk.chipsekiz.interpreter.Interpreter;
import dev.krk.chipsekiz.interpreter.InterpreterFactory;
import dev.krk.chipsekiz.sprites.CharacterSprites;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Emulator {
    private final IHal hal;
    private volatile boolean isStopping;
    private volatile int cpuFrequency;
    private boolean debuggerEnabled;

    public Emulator(IHal hal) {
        this.hal = hal;
        this.isStopping = false;
        this.debuggerEnabled = false;
        this.cpuFrequency = 0;
    }

    public void setDebuggerEnabled(boolean enabled) {
        debuggerEnabled = enabled;
    }

    public int getCpuFrequency() {
        return cpuFrequency;
    }

    public void stop() {
        isStopping = true;
    }

    public void run() {
        byte[] program;
        try {
            program = getClass().getClassLoader().getResourceAsStream("demo/chipsekiz-demo.ch8")
                .readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        IDebugger debugger = null;
        final DebuggerWindow[] debuggerWindow = new DebuggerWindow[1];
        if (debuggerEnabled) {
            debugger = vm -> debuggerWindow[0] = new DebuggerWindow(vm);
        }

        Interpreter chipsekiz =
            InterpreterFactory.create(hal, CharacterSprites.getAddressLocator(), null, debugger);

        chipsekiz.load(0x200, program);

        final int[] cycles = {0};

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                cpuFrequency = cycles[0];
                cycles[0] = 0;
            }
        }, 0, 1000);

        // ~500Hz CPU speed.
        final long frequency = 500; // Hz
        final double BudgetNs = 1e9 / frequency;

        double done = 0;
        long prev = System.nanoTime();
        while (!isStopping) {
            long now = System.nanoTime();
            long diff = now - prev;
            done += diff / BudgetNs;
            prev = now;

            if (done >= 1) {
                chipsekiz.tick();
                if (debuggerEnabled) {
                    debuggerWindow[0].requestRepaint();
                }
                cycles[0]++;
                done = 0;
            }
        }
    }
}
