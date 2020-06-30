package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.superchip.interpreter.Resolution;

import javax.sound.sampled.LineUnavailableException;

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

        EmulatorWindow win = new EmulatorWindow(factory, options, resolution);
        win.run();
    }
}
