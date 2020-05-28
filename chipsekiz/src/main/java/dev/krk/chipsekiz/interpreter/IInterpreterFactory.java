package dev.krk.chipsekiz.interpreter;

public interface IInterpreterFactory {
    IInterpreter create(IHal hal);
}
