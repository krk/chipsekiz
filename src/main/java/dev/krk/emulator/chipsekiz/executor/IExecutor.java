package dev.krk.emulator.chipsekiz.executor;

import dev.krk.emulator.chipsekiz.interpreter.IHal;
import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import dev.krk.emulator.chipsekiz.vm.VM;

public interface IExecutor {
    void execute(VM vm, IHal hal, Opcode opcode);
}
