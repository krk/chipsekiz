package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.IInterpreter;


public class ChipVariation implements IChipVariation {
    private final IInterpreter interpreter;
    private final int displayWidth;
    private final int displayHeight;
    private final int demoOrigin;
    private final byte[] demoProgram;

    ChipVariation(IInterpreter interpreter, int displayWidth, int displayHeight) {
        this(interpreter, displayWidth, displayHeight, 0, null);
    }

    ChipVariation(IInterpreter interpreter, int displayWidth, int displayHeight, int demoOrigin,
        byte[] demoProgram) {
        this.interpreter = interpreter;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.demoOrigin = demoOrigin;
        this.demoProgram = demoProgram;
    }

    public boolean hasDemoProgram() {
        return demoProgram != null;
    }

    public IInterpreter getInterpreter() {
        return interpreter;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public int getDemoOrigin() {
        return demoOrigin;
    }

    public byte[] getDemoProgram() {
        return demoProgram;
    }
}
