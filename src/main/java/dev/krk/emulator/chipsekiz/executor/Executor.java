package dev.krk.emulator.chipsekiz.executor;

import dev.krk.emulator.chipsekiz.interpreter.IHal;
import dev.krk.emulator.chipsekiz.opcodes.Op1NNN;
import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import dev.krk.emulator.chipsekiz.vm.VM;

public class Executor implements IExecutor {
    @Override public void execute(VM vm, IHal hal, Opcode opcode) {
        if (opcode instanceof Op1NNN o) {
            vm.setPC(o.address());
        } else {
            throw new IllegalArgumentException("unsupported opcode.");
        }
    }
}
