package dev.krk.emulator.chipsekiz.opcodes;

/**
 * Stores the binary-coded decimal representation of VX, with the most significant of three
 * digits at the address in I, the middle digit at I plus 1, and the least significant digit at I
 * plus 2. (In other words, take the decimal representation of VX, place the hundreds digit in
 * memory at location in I, the tens digit at location I+1, and the ones digit at location I+2.)
 */
public class OpFX33 extends OpPXQR {
    public OpFX33(int vx) {
        super(0xF, vx, 3, 3);
    }
}
