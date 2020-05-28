package dev.krk.chipsekiz.superchip.vm;

import dev.krk.chipsekiz.vm.IVirtualMachine;

public interface ISuperChipVirtualMachine extends IVirtualMachine {
    void swapRegisterBank();
}
