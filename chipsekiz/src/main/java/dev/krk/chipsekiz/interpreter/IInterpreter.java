package dev.krk.chipsekiz.interpreter;

public interface IInterpreter {
    void tick();

    InterpreterStatus getStatus();

    void load(int lastLoadedOrigin, byte[] lastLoadedProgram);

    void setDebugger(IDebugger debugger);
}
