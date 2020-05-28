package dev.krk.chipsekiz.interpreter;

public interface IChipVariation {
    IInterpreter createInterpreter(IHal hal);

    int getDisplayWidth();

    int getDisplayHeight();

    boolean hasDemoProgram();

    int getDemoOrigin();

    byte[] getDemoProgram();
}
