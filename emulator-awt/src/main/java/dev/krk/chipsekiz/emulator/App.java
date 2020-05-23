package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IInterpreter;

import javax.sound.sampled.LineUnavailableException;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws LineUnavailableException {
        EmulatorOptions options = new EmulatorOptions(true);
        runEmulator(options);
    }

    private static void runEmulator(EmulatorOptions options) throws LineUnavailableException {
        final Tone tone = new Tone(1600);

        EmulatorCanvas canvas = new EmulatorCanvas(64, 32, 12, 12, Color.WHITE, Color.BLACK);
        Hal hal = new Hal(canvas, tone);
        IDebugger debugger = null;
        if (options.isDebuggerEnabled()) {
            debugger = new Debugger();
        }

        IChipVariation variation = ChipVariationFactory.createChip8(hal);

        IInterpreter interpreter = variation.getInterpreter();
        interpreter.setDebugger(debugger);

        Emulator emulator = new Emulator(interpreter);
        IEmulatorController emulatorController =
            new EmulatorController(emulator, interpreter, debugger, hal, tone);

        if (variation.hasDemoProgram()) {
            interpreter.load(variation.getDemoOrigin(), variation.getDemoProgram());
            emulatorController
                .setLoadedProgram(variation.getDemoOrigin(), variation.getDemoProgram());
        }

        EmulatorWindow win = new EmulatorWindow(canvas, emulatorController);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                win.setTitle(
                    String.format("chipsekiz emulator - %d Hz", emulator.getActualFrequency()));
            }
        }, 0, 100);

        if (!variation.hasDemoProgram()) {
            emulator.pause();
        }
        emulator.run();

        if (tone != null) {
            tone.close();
        }
    }
}
