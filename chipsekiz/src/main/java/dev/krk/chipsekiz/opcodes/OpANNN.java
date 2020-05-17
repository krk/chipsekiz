package dev.krk.chipsekiz.opcodes;

/**
 * Sets I to the address NNN.
 */
public class OpANNN extends OpPNNN {
    public OpANNN(short address) {
        super(0xA, address);
    }

    @Override public String toString() {
        return String.format("LD I, 0x%03X", address());
    }
}
