package dev.krk.chipsekiz.opcodes;

/**
 * Clears the screen.
 */
public class Op00E0 extends Opcode {
    public Op00E0() {
        super();
    }

    @Override public short getValue() {
        return (short) 0x00E0;
    }

    @Override public String encode() {
        return "00E0";
    }

    @Override public String toString() {
        return "CLS";
    }
}
