package dev.krk.emulator.chipsekiz.opcodes;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class OpPNNN extends Opcode {
    private final byte p;

    public OpPNNN(int p, int address) {
        super(address & 0xFFF);
        checkArgument(p >= 0 && p <= 0xF, "p out of bounds");
        checkArgument(address >= 0 && address <= 0xFFF, "address out of bounds");

        this.p = (byte) p;
    }

    public int address() {
        return super.getAddress().get();
    }

    @Override public short getValue() {
        return (short) (p << 12 | address());
    }

    @Override public String encode() {
        return String.format("%1X%03X", p, address());
    }
}
