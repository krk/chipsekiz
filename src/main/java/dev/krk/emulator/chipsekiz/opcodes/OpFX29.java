package dev.krk.emulator.chipsekiz.opcodes;

/**
 * Sets I to the location of the sprite for the character in VX. Characters 0-F (in hexadecimal)
 * are represented by a 4x5 font.
 */
public class OpFX29 extends OpPXQR {
    public OpFX29(int vx) {
        super(0xF, vx, 2, 9);
    }
}
