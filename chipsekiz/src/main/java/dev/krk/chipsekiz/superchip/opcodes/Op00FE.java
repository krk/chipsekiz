package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.Opcode;

/**
 * Disable high resolution graphics mode and return to 64x32.
 */
public class Op00FE extends Opcode {
    public Op00FE() {
        super();
    }

    @Override public short getValue() {
        return (short) 0x00FE;
    }

    @Override public String encode() {
        return "00FE";
    }

    @Override public String toString() {
        return "LOW";
    }
}
