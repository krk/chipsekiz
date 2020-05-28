package dev.krk.chipsekiz.superchip.interpreter;

import dev.krk.chipsekiz.interpreter.IHal;

public interface ISuperChipHal extends IHal, ISuperChipScreenHal {
    void exit();
}
