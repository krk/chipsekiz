package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.ChipVariationFactory;
import dev.krk.chipsekiz.interpreter.IChipVariation;
import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IInterpreter;

import java.awt.Color;


public class Chip8EmulatableFactory implements IEmulatableFactory {
    public IEmulatable create(EmulatorOptions options, Tone tone) {
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
