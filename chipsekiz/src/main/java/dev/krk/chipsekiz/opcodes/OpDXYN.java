package dev.krk.chipsekiz.opcodes;

public class OpDXYN extends Opcode {
    public OpDXYN(int vx, int vy, int imm) {
        super(vx, vy, imm);
        if (vx < 0 || vx > 0xF) throw new IllegalArgumentException("register index out of bounds");
        if (vy < 0 || vy > 0xF) throw new IllegalArgumentException("register index out of bounds");
        if (imm < 0 || imm > 0xF) throw new IllegalArgumentException("immediate index out of bounds");
    }

    public int vx() {
        return super.getVx();
    }

    public int vy() {
        return super.getVy();
    }

    public int imm() {
        return super.getAddress();
    }

    @Override public short getValue() {
        return (short) (0xD << 12 | vx() << 8 | vy() << 4 | imm());
    }

    @Override public String encode() {
        return String.format("D%1X%1X%1X", vx(), vy(), imm());
    }

    @Override public String toString() {
        return String.format("DRW V%X, V%X, %d", vx(), vy(), imm());
    }
}
