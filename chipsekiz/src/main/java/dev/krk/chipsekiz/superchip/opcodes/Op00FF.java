package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.Opcode;

/**
 * Enable 128x64 high resolution graphics mode.
 */
public class Op00FF extends Opcode {
    public Op00FF() {
        super();
    }

    @Override public short getValue() {
        return (short) 0x00FF;
    }

    @Override public String encode() {
        return "00FF";
    }

    @Override public String toString() {
        return "HIGH";
    }
}
