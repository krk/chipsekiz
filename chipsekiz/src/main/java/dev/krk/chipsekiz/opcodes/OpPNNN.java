package dev.krk.chipsekiz.opcodes;

public abstract class OpPNNN extends Opcode {
    private final byte p;

    public OpPNNN(int p, short address) {
        super(address & 0xFFF);
        if (p < 0 || p > 0xF) throw new IllegalArgumentException("p out of bounds");
        if (address < 0 || address > 0xFFF) throw new IllegalArgumentException("address out of bounds");

        this.p = (byte) p;
    }

    public short address() {
        return super.getAddress().shortValue();
    }

    @Override public short getValue() {
        return (short) (p << 12 | address());
    }

    @Override public String encode() {
        return String.format("%1X%03X", p, address());
    }
}
