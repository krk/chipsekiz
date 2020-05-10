package dev.krk.emulator.chipsekiz.tracer;

import dev.krk.emulator.chipsekiz.opcodes.Opcode;

public interface ITracer {
    void trace(int address, Opcode opcode);
}
