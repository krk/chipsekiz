package dev.krk.chipsekiz.tracer;

import dev.krk.chipsekiz.opcodes.Opcode;

public interface ITracer {
    void trace(int address, Opcode opcode);
}
