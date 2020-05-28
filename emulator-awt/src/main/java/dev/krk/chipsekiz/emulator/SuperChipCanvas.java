package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.hal.Direction;
import dev.krk.chipsekiz.superchip.interpreter.ISuperChipScreenHal;
import dev.krk.chipsekiz.superchip.interpreter.Resolution;

import java.awt.Color;

public class SuperChipCanvas extends EmulatorCanvas implements ISuperChipScreenHal {
    private Resolution resolution;

    SuperChipCanvas(int emulatorWidth, int emulatorHeight, int scaleX, int scaleY, Color emptyColor,
        Color occupiedColor) {
        super(emulatorWidth, emulatorHeight, scaleX, scaleY, emptyColor, occupiedColor);

        resolution = Resolution.Low;
    }

    @Override public void scrollDown(byte n) {
        renderer.scroll(Direction.Down, n);
    }

    @Override public void scrollLeft() {
        renderer.scroll(Direction.Left, 4);
    }

    @Override public void scrollRight() {
        renderer.scroll(Direction.Right, 4);
    }

    @Override public void setResolution(Resolution resolution) {
        if (this.resolution != resolution) {
            resizeEmulator(resolution == Resolution.High ? emulatorWidth * 2 : emulatorWidth / 2,
                resolution == Resolution.High ? emulatorHeight * 2 : emulatorHeight / 2);

            this.resolution = resolution;
        }
    }
}
