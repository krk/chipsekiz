package dev.krk.chipsekiz.interpreter;

public interface IChipVariation {
    String getName();

    IInterpreter createInterpreter(IHal hal);

    int getDisplayWidth();

    int getDisplayHeight();

    boolean hasDemoProgram();

    int getDemoOrigin();

    byte[] getDemoProgram();
}
