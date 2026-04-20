package dev.krk.chipsekiz.opcodes;

public abstract class OpPXYQ extends Opcode {
    private final byte p;
    private final byte q;

    public OpPXYQ(int p, int vx, int vy, int q) {
        super(vx, vy, null);
        if (p < 0 || p > 0xF) throw new IllegalArgumentException("p out of bounds");
        if (vx < 0 || vx > 0xF) throw new IllegalArgumentException("register index out of bounds");
        if (vy < 0 || vy > 0xF) throw new IllegalArgumentException("register index out of bounds");
        if (q < 0 || q > 0xF) throw new IllegalArgumentException("q out of bounds");

        this.p = (byte) p;
        this.q = (byte) q;
    }

    public int vx() {
        return super.getVx();
    }

    public int vy() {
        return super.getVy();
    }

    @Override public short getValue() {
        return (short) (p << 12 | vx() << 8 | vy() << 4 | q);
    }

    @Override public String encode() {
        return String.format("%1X%1X%1X%1X", p, vx(), vy(), q);
    }
}
