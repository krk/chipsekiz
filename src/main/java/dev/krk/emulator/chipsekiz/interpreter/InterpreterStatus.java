package dev.krk.emulator.chipsekiz.interpreter;

public enum InterpreterStatus {
    READY,

    /**
     * Awaiting key input.
     */
    BLOCKED,

    /**
     * Self-jump, cannot change PC.
     */
    HALTED,
}
