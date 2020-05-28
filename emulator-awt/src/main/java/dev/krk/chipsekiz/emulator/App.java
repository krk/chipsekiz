package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.superchip.interpreter.Resolution;

import javax.sound.sampled.LineUnavailableException;

import java.util.Timer;
import java.util.TimerTask;

public class App {
    public static void main(String[] args) throws LineUnavailableException {
        EmulatorOptions options = new EmulatorOptions(true);

        Resolution resolution = Resolution.Low;
        IEmulatableFactory factory;
        if (args.length == 0) {
            factory = new Chip8EmulatableFactory();
        } else {
            if (args[0].equals("--chip8")) {
                factory = new Chip8EmulatableFactory();
            } else if (args[0].equals("--superchip8")) {
                factory = new SuperChip8EmulatableFactory();
                resolution = Resolution.High;
            } else {
                factory = new Chip8EmulatableFactory();
            }
        }

        runEmulator(factory, options, resolution);
    }

    private static void runEmulator(IEmulatableFactory factory, EmulatorOptions options,
        Resolution resolution) throws LineUnavailableException {
        final Tone tone = new Tone(1600);

        IEmulatable e = factory.create(options, tone);
        IEmulatorController controller = e.getController();
        EmulatorWindow win = new EmulatorWindow(e.getCanvas(), controller, resolution);

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
}
