package dev.krk.chipsekiz.superchip.opcodes;


import dev.krk.chipsekiz.opcodes.Opcode;

public class Op00CN extends Opcode {
    public Op00CN(byte address) {
        super(address & 0xF);
        if (address < 0 || address > 0xF) throw new IllegalArgumentException("immediate out of bounds");
    }

    public byte imm() {
        return super.getAddress().byteValue();
    }

    @Override public short getValue() {
        return (short) (0x00C0 | imm());
    }

    @Override public String encode() {
        return String.format("00C%01X", imm());
    }

    @Override public String toString() {
        return String.format("SCD 0x%01X", imm());
    }
}
