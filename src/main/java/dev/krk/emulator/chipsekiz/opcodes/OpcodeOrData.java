package dev.krk.emulator.chipsekiz.opcodes;

import com.google.auto.value.AutoOneOf;

@AutoOneOf(OpcodeOrData.Kind.class) public abstract class OpcodeOrData {
    public enum Kind {OPCODE, DATA}

    public abstract Kind getKind();

    public abstract Opcode opcode();

    public abstract DataWord data();

    public static OpcodeOrData ofOpcode(Opcode o) {
        return AutoOneOf_OpcodeOrData.opcode(o);
    }

    public static OpcodeOrData ofData(DataWord dw) {
        return AutoOneOf_OpcodeOrData.data(dw);
    }

    public short getValue() {
        return switch (getKind()) {
            case DATA -> data().getValue();
            case OPCODE -> opcode().getValue();
        };
    }

    public String encode() {
        return switch (getKind()) {
            case DATA -> data().encode();
            case OPCODE -> opcode().encode();
        };
    }
}
