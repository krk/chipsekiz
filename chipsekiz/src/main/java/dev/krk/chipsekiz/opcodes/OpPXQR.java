package dev.krk.chipsekiz.opcodes;

public abstract class OpPXQR extends Opcode {
    private final byte p;
    private final byte q;
    private final byte r;

    public OpPXQR(int p, int vx, int q, int r) {
        super(vx, null, null);
        if (p < 0 || p > 0xF) throw new IllegalArgumentException("p out of bounds");
        if (vx < 0 || vx > 0xF) throw new IllegalArgumentException("register index out of bounds");
        if (q < 0 || q > 0xF) throw new IllegalArgumentException("q out of bounds");
        if (r < 0 || r > 0xF) throw new IllegalArgumentException("r out of bounds");

        this.p = (byte) p;
        this.q = (byte) q;
        this.r = (byte) r;
    }

    public int vx() {
        return super.getVx();
    }

    @Override public short getValue() {
        return (short) (p << 12 | vx() << 8 | q << 4 | r);
    }

    @Override public String encode() {
        return "%1X%1X%1X%1X".formatted(p, vx(), q, r);
    }
}
