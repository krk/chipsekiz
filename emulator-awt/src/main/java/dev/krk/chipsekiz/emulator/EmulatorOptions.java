package dev.krk.chipsekiz.emulator;

public class EmulatorOptions {
    private final boolean debuggerEnabled;

    public EmulatorOptions(boolean debuggerEnabled) {
        this.debuggerEnabled = debuggerEnabled;
    }

    public boolean isDebuggerEnabled() {
        return debuggerEnabled;
    }
}
