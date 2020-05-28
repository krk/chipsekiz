package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.tracer.ITracer;

public interface IInterpreter {
    void tick();

    InterpreterStatus getStatus();

    void load(int lastLoadedOrigin, byte[] lastLoadedProgram);

    void setDebugger(IDebugger debugger);

    void setTracer(ITracer tracer);
}
