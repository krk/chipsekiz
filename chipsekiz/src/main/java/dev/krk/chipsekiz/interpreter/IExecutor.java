package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.vm.VM;

public interface IExecutor {
    void execute(VM vm, IHal hal, ICharacterAddressLocator characterAddressLocator, Opcode opcode);
}
