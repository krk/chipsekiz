package dev.krk.emulator.chipsekiz.opcodes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.padStart;

public class Op00E0 extends Opcode {
    public Op00E0() {
        super();
    }

    @Override public short getValue() {
        return (short) 0x00E0;
    }

    @Override public String toString() {
        return "00E0";
    }
}
