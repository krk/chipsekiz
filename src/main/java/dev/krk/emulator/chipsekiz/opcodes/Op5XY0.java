package dev.krk.emulator.chipsekiz.opcodes;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.padStart;

public class Op5XY0 extends OpPXYQ {
    public Op5XY0(int vx, int vy) {
        super(5, vx, vy, 0);
    }
}
