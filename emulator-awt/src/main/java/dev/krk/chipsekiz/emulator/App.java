package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.ChipVariationFactory;
import dev.krk.chipsekiz.interpreter.IChipVariation;
import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IInterpreter;

import javax.sound.sampled.LineUnavailableException;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    public static void main(String[] args) throws LineUnavailableException {
        EmulatorOptions options = new EmulatorOptions(true);
        runEmulator(options);
    }

    private static void runEmulator(EmulatorOptions options) throws LineUnavailableException {
        final Tone tone = new Tone(1600);

        Emulatable e = createEmulatable(options, tone);
        IEmulatorController controller = e.getController();
        EmulatorWindow win = new EmulatorWindow(e.getCanvas(), controller);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                win.setTitle(
                    String.format("chipsekiz emulator - %d Hz", controller.getActualFrequency()));
            }
        }, 0, 100);

        if (!e.hasDemoProgram()) {
            controller.pause();
        }
        controller.run();

        tone.close();
    }

    private static Emulatable createEmulatable(EmulatorOptions options, Tone tone) {
        IChipVariation variation = ChipVariationFactory.createChip8();

        EmulatorCanvas canvas =
            new EmulatorCanvas(variation.getDisplayWidth(), variation.getDisplayHeight(), 12, 12,
                Color.WHITE, Color.BLACK);
        Hal hal = new Hal(canvas, tone);
        IDebugger debugger = null;
        if (options.isDebuggerEnabled()) {
            debugger = new Debugger();
        }

        IInterpreter interpreter = variation.createInterpreter(hal);
        interpreter.setDebugger(debugger);

        Emulator emulator = new Emulator(interpreter);
        IEmulatorController emulatorController =
            new EmulatorController(emulator, interpreter, debugger, hal, tone);

        if (variation.hasDemoProgram()) {
            interpreter.load(variation.getDemoOrigin(), variation.getDemoProgram());
            emulatorController
                .setLoadedProgram(variation.getDemoOrigin(), variation.getDemoProgram());
        }

        return new Emulatable(emulatorController, canvas, variation.hasDemoProgram());
    }
}


class Emulatable {
    private final IEmulatorController controller;
    private final EmulatorCanvas canvas;
    private final boolean hasDemoProgram;

    Emulatable(IEmulatorController controller, EmulatorCanvas canvas, boolean hasDemoProgram) {
        this.controller = controller;
        this.canvas = canvas;
        this.hasDemoProgram = hasDemoProgram;
    }

    public IEmulatorController getController() {
        return controller;
    }

    public EmulatorCanvas getCanvas() {
        return canvas;
    }

    public boolean hasDemoProgram() {
        return hasDemoProgram;
    }
}
