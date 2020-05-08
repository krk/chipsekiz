package dev.krk.emulator.chipsekiz.opcodes;

public class DataWord {
    private final short value;

    public DataWord(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public String encode() {
        return String.format("%04X", getValue());
    }

    @Override public String toString() {
        return encode();
    }
}
