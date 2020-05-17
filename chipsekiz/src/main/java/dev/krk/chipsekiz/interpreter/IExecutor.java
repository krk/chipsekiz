package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.vm.IVirtualMachine;

public interface IExecutor {
    void execute(IVirtualMachine vm, IHal hal, ICharacterAddressLocator characterAddressLocator, Opcode opcode);
}
