package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.ChipVariationFactory;
import dev.krk.chipsekiz.interpreter.IChipVariation;
import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IInterpreter;

import java.awt.Color;


public class SuperChip8EmulatableFactory implements IEmulatableFactory {
    public IEmulatable create(EmulatorOptions options, Tone tone) {
        IChipVariation variation = ChipVariationFactory.createSuperChip8();

        SuperChipCanvas canvas =
            new SuperChipCanvas(variation.getDisplayWidth(), variation.getDisplayHeight(), 12, 12,
                Color.WHITE, Color.BLACK);

        SuperChipHal hal = new SuperChipHal(canvas, tone);
        IDebugger debugger = null;
        if (options.isDebuggerEnabled()) {
            debugger = new Debugger();
        }

        IInterpreter interpreter = variation.createInterpreter(hal);
        interpreter.setDebugger(debugger);

        Emulator emulator = new Emulator(interpreter);
        IEmulatorController emulatorController =
            new EmulatorController(emulator, interpreter, debugger, hal, tone);

        hal.setExiter(new IExiter() {
            @Override public void exit() {
                // Do not stop, only pause so last screen contents stay visible.
                emulatorController.pause();
            }
        });

        if (variation.hasDemoProgram()) {
            interpreter.load(variation.getDemoOrigin(), variation.getDemoProgram());
            emulatorController
                .setLoadedProgram(variation.getDemoOrigin(), variation.getDemoProgram());
        }

        return new Emulatable(emulatorController, canvas, variation.hasDemoProgram());
    }
}
