package dev.krk.emulator.chipsekiz.opcodes;

public class Op00EE extends Opcode {
    public Op00EE() {
        super();
    }

    @Override public short getValue() {
        return (short) 0x00EE;
    }

    @Override public String toString() {
        return "00EE";
    }
}

