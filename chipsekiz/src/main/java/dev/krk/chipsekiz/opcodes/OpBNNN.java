package dev.krk.chipsekiz.opcodes;

/**
 * Jumps to the address NNN plus V0.
 */
public class OpBNNN extends OpPNNN {
    public OpBNNN(short address) {
        super(0xB, address);
    }

    @Override public String toString() {
        return String.format("JP V0, 0x%03X", address());
    }
}
