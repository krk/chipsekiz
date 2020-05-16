package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IHal;
import dev.krk.chipsekiz.interpreter.IScreenHal;

import java.util.Optional;
import java.util.Random;

public class Hal implements IHal {

    private final Random random;
    private final IScreenHal screen;

    Hal(IScreenHal screen) {
        random = new Random();
        this.screen = screen;
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

    }

    @Override public Optional<Byte> getKey() {
        return Optional.empty();
    }
}
