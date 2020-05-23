package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.IHal;
import dev.krk.chipsekiz.interpreter.IScreenHal;

import java.util.Optional;
import java.util.Random;

public class Hal implements IHal {
    private final Random random;
    private final IScreenHal screen;
    private final Tone tone;
    private Byte key;

    Hal(IScreenHal screen, Tone tone) {
        random = new Random();
        this.screen = screen;
        this.tone = tone;
    }

    @Override public byte getRand() {
        return (byte) random.nextInt(0X100);
    }

    @Override public void clearScreen() {
        screen.clearScreen();
    }

    @Override public boolean draw(byte x, byte y, boolean value) {
        return screen.draw(x, y, value);
    }

    @Override public void sound(boolean active) {
        if (tone == null) {
            return;
        }

        if (active) {
            tone.start();
        } else {
            tone.stop();
        }
    }

    @Override public Optional<Byte> getKey() {
        return Optional.ofNullable(key);
    }

    public void keyUp() {
        key = null;
    }

    public void keyDown(Byte key) {
        this.key = key;
    }
}
