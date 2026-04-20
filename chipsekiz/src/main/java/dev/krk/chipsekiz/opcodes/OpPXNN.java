package dev.krk.chipsekiz.opcodes;

public abstract class OpPXNN extends Opcode {
    private final byte p;

    public OpPXNN(int p, int vx, byte imm) {
        super(vx, null, Byte.toUnsignedInt(imm));
        if (p < 0 || p > 0xF) throw new IllegalArgumentException("p out of bounds");
        if (vx < 0 || vx > 0xF) throw new IllegalArgumentException("register index out of bounds");

        this.p = (byte) p;
    }

    public int vx() {
        return super.getVx();
    }

    public byte imm() {
        return super.getAddress().byteValue();
    }

    @Override public short getValue() {
        return (short) (p << 12 | vx() << 8 | (imm() & 0xFF));
    }

    @Override public String encode() {
        return String.format("%1X%1X%02X", p, vx(), imm());
    }
}

