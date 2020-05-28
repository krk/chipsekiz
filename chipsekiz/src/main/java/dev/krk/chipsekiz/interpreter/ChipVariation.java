package dev.krk.chipsekiz.interpreter;

public class ChipVariation<THal extends IHal> implements IChipVariation<THal> {
    private final IInterpreterFactory<THal> interpreterFactory;
    private final int displayWidth;
    private final int displayHeight;
    private final int demoOrigin;
    private final byte[] demoProgram;
    private final String name;

    ChipVariation(String name, IInterpreterFactory<THal> interpreterFactory, int displayWidth,
        int displayHeight) {
        this(name, interpreterFactory, displayWidth, displayHeight, 0, null);
    }

    ChipVariation(String name, IInterpreterFactory<THal> interpreterFactory, int displayWidth,
        int displayHeight, int demoOrigin, byte[] demoProgram) {
        this.name = name;
        this.interpreterFactory = interpreterFactory;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.demoOrigin = demoOrigin;
        this.demoProgram = demoProgram;
    }

    public boolean hasDemoProgram() {
        return demoProgram != null;
    }

    public IInterpreter createInterpreter(THal hal) {
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

    public String getName() {
        return name;
    }
}
