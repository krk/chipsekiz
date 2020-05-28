package dev.krk.chipsekiz.superchip.hal;

import dev.krk.chipsekiz.hal.Direction;
import dev.krk.chipsekiz.hal.Framebuffer;
import dev.krk.chipsekiz.hal.FramebufferHal;
import dev.krk.chipsekiz.superchip.interpreter.ISuperChipHal;
import dev.krk.chipsekiz.superchip.interpreter.Resolution;

public class SuperChipFramebufferHal extends FramebufferHal implements ISuperChipHal {
    public SuperChipFramebufferHal() {
        this(Resolution.Low);
    }

    public SuperChipFramebufferHal(Resolution resolution) {
        super(1, 1);
        setResolution(resolution);
    }

    @Override public void exit() {
        // NOP.
    }

    @Override public void scrollDown(byte n) {
        this.fb.scroll(Direction.Down, n);
    }

    @Override public void scrollLeft() {
        this.fb.scroll(Direction.Left, 4);
    }

    @Override public void scrollRight() {
        this.fb.scroll(Direction.Right, 4);
    }

    @Override public void setResolution(Resolution resolution) {
        this.fb = new Framebuffer(resolution == Resolution.High ? 128 : 64,
            resolution == Resolution.High ? 64 : 32);
    }

    public String renderFramebuffer() {
        return super.renderFramebuffer();
    }
}
