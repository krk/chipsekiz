package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.vm.IVirtualMachine;

public interface IExecutor {
    void execute(Opcode opcode);

    void setVM(IVirtualMachine vm);
}
