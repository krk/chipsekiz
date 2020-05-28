package dev.krk.chipsekiz.interpreter;

public interface IInterpreterFactory<THal> {
    IInterpreter create(THal hal);
}
