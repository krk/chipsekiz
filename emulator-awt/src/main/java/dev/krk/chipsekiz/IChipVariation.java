package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IInterpreter;

public interface IChipVariation {
    IInterpreter getInterpreter();

    int getDisplayWidth();

    int getDisplayHeight();

    boolean hasDemoProgram();

    int getDemoOrigin();

    byte[] getDemoProgram();
}
