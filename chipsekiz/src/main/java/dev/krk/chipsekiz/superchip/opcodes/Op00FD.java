package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.Opcode;

/**
 * Exit the Chip8/SuperChip interpreter.
 */
public class Op00FD extends Opcode {
    public Op00FD() {
        super();
    }

    @Override public short getValue() {
        return (short) 0x00FD;
    }

    @Override public String encode() {
        return "00FD";
    }

    @Override public String toString() {
        return "EXIT";
    }
}
