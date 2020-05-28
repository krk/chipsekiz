package dev.krk.chipsekiz.superchip.interpreter;

import dev.krk.chipsekiz.interpreter.IScreenHal;

public interface ISuperChipScreenHal extends IScreenHal {
    void scrollDown(byte n);

    void scrollLeft();

    void scrollRight();

    void setResolution(Resolution resolution);
}
