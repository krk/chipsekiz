package dev.krk.chipsekiz.emulator;

public interface IEmulatable {
    IEmulatorController getController();

    EmulatorCanvas getCanvas();

    boolean hasDemoProgram();
}
