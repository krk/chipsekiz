package dev.krk.chipsekiz.opcodes;

/**
 * A key press is awaited, and then stored in VX. (Blocking Operation. All instruction halted until
 * next key event).
 */
public class OpFX0A extends OpPXQR {
    public OpFX0A(int vx) {
        super(0xF, vx, 0, 0xA);
    }

    @Override public String toString() {
        return String.format("LD V%X, K", vx());
    }
}
