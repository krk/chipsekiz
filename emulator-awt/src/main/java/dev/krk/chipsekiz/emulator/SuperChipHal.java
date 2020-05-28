package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.superchip.interpreter.ISuperChipHal;
import dev.krk.chipsekiz.superchip.interpreter.ISuperChipScreenHal;
import dev.krk.chipsekiz.superchip.interpreter.Resolution;


public class SuperChipHal extends Hal implements ISuperChipHal {
    private IExiter exiter;
    private final ISuperChipScreenHal screen;

    SuperChipHal(ISuperChipScreenHal screen, Tone tone) {
        super(screen, tone);
        this.screen = screen;
    }

    @Override public void exit() {
        if (exiter != null) {
            exiter.exit();
        }
    }

    @Override public void scrollDown(byte n) {
        screen.scrollDown(n);
    }

    @Override public void scrollLeft() {
        screen.scrollLeft();
    }

    @Override public void scrollRight() {
        screen.scrollRight();
    }

    @Override public void setResolution(Resolution resolution) {
        screen.setResolution(resolution);
    }

    public void setExiter(IExiter exiter) {
        this.exiter = exiter;
    }
}
