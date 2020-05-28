package dev.krk.chipsekiz.interpreter;

public interface IChipVariation<THal extends IHal> {
    String getName();

    IInterpreter createInterpreter(THal hal);

    int getDisplayWidth();

    int getDisplayHeight();

    boolean hasDemoProgram();

    int getDemoOrigin();

    byte[] getDemoProgram();
}
