package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.Opcode;

/**
 * Scroll the display left by 4 pixels.
 */
public class Op00FC extends Opcode {
    public Op00FC() {
        super();
    }

    @Override public short getValue() {
        return (short) 0x00FC;
    }

    @Override public String encode() {
        return "00FC";
    }

    @Override public String toString() {
        return "SCL";
    }
}
