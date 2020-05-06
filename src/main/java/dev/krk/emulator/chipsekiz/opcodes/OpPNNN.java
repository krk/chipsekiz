package dev.krk.emulator.chipsekiz.opcodes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.padStart;

public abstract class OpPNNN extends Opcode {
    private final char p;

    public OpPNNN(char p, int address) {
        super(address);
        checkArgument(address == (address & 0xFFF), "address out of bounds");
        this.p = p;
    }

    public int address() {
        return super.getAddress().get();
    }

    @Override public String toString() {
        return p + padStart(Integer.toHexString(address()).toUpperCase(), 3, '0');
    }
}
