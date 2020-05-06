package dev.krk.emulator.chipsekiz.opcodes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.padStart;

public class Op1NNN extends Opcode {
    public Op1NNN(int address) {
        super(address);
        checkArgument(address == (address & 0xFFF), "address out of bounds");
    }

    public int address() {
        return super.getAddress().get();
    }

    @Override public String toString() {
        return "1" + padStart(Integer.toHexString(address()).toUpperCase(), 3, '0');
    }
}
