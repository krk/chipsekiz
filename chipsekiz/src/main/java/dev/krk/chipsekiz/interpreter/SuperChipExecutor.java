package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.vm.IVirtualMachine;

public class SuperChipExecutor implements IExecutor {
    private final IExecutor chip8Executor;

    SuperChipExecutor() {
        this.chip8Executor = new Executor(false, true);
    }

    @Override public void execute(IVirtualMachine vm, IHal hal,
        ICharacterAddressLocator characterAddressLocator, Opcode opcode) {



        chip8Executor.execute(vm, hal, characterAddressLocator, opcode);
    }
}
