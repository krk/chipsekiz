package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.Opcode;

/**
 * Scroll the display right by 4 pixels.
 */
public class Op00FB extends Opcode {
    public Op00FB() {
        super();
    }

    @Override public short getValue() {
        return (short) 0x00FB;
    }

    @Override public String encode() {
        return "00FB";
    }

    @Override public String toString() {
        return "SCR";
    }
}
