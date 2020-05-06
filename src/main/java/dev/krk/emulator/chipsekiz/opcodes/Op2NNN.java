package dev.krk.emulator.chipsekiz.opcodes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.padStart;

public class Op2NNN extends Opcode {
    public Op2NNN(int address) {
        super(address);
        checkArgument(address == (address & 0xFFF), "address out of bounds");
    }

    public int address() {
        return super.getAddress().get();
    }

    @Override public String toString() {
        return "2" + padStart(Integer.toHexString(address()).toUpperCase(), 3, '0');
    }
}
