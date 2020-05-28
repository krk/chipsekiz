package dev.krk.chipsekiz.emulator;

public class Emulatable implements IEmulatable {
    private final IEmulatorController controller;
    private final EmulatorCanvas canvas;
    private final boolean hasDemoProgram;

    Emulatable(IEmulatorController controller, EmulatorCanvas canvas, boolean hasDemoProgram) {
        this.controller = controller;
        this.canvas = canvas;
        this.hasDemoProgram = hasDemoProgram;
    }

    @Override public IEmulatorController getController() {
        return controller;
    }

    @Override public EmulatorCanvas getCanvas() {
        return canvas;
    }

    @Override public boolean hasDemoProgram() {
        return hasDemoProgram;
    }
}
