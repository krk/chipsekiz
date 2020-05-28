package dev.krk.chipsekiz.interpreter;

public class ChipVariation implements IChipVariation {
    private final IInterpreterFactory interpreterFactory;
    private final int displayWidth;
    private final int displayHeight;
    private final int demoOrigin;
    private final byte[] demoProgram;

    ChipVariation(IInterpreterFactory interpreterFactory, int displayWidth, int displayHeight) {
        this(interpreterFactory, displayWidth, displayHeight, 0, null);
    }

    ChipVariation(IInterpreterFactory interpreterFactory, int displayWidth, int displayHeight,
        int demoOrigin, byte[] demoProgram) {
        this.interpreterFactory = interpreterFactory;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.demoOrigin = demoOrigin;
        this.demoProgram = demoProgram;
    }

    public boolean hasDemoProgram() {
        return demoProgram != null;
    }

    public IInterpreter createInterpreter(IHal hal) {
        return interpreterFactory.create(hal);
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
