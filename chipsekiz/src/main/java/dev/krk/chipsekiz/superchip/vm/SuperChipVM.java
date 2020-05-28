package dev.krk.chipsekiz.superchip.vm;

import dev.krk.chipsekiz.vm.IVMDebugger;
import dev.krk.chipsekiz.vm.VM;

public class SuperChipVM extends VM implements ISuperChipVirtualMachine {
    private byte[] otherBank;

    public SuperChipVM() {
        this(0x200, new byte[0x1000], null);
    }

    public SuperChipVM(int origin, byte[] memory, IVMDebugger debugger) {
        super(origin, memory, debugger);
        this.otherBank = new byte[16];
    }

    @Override public void swapRegisterBank() {
        byte[] bank = new byte[16];
        for (int i = 0; i < 16; i++) {
            bank[i] = getRegister(i);
            setRegister(i, otherBank[i]);
        }
        otherBank = bank;
    }
}
