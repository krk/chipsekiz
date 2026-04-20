package dev.krk.chipsekiz.opcodes;

public sealed interface Word permits Word.Op, Word.Data {
    short getValue();
    String encode();

    default Opcode opcode() { throw new UnsupportedOperationException(); }

    static Word op(Opcode o) { return new Op(o); }
    static Word data(short value) { return new Data(value); }

    record Op(Opcode opcode) implements Word {
        public short getValue() { return opcode.getValue(); }
        public String encode() { return opcode.encode(); }
    }

    record Data(short value) implements Word {
        public short getValue() { return value; }
        public String encode() { return "%04X".formatted(getValue()); }
    }
}
