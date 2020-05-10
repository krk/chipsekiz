package dev.krk.emulator.chipsekiz.interpreter;

import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import dev.krk.emulator.chipsekiz.vm.VM;

public interface IExecutor {
    void execute(VM vm, IHal hal, Opcode opcode);
}
