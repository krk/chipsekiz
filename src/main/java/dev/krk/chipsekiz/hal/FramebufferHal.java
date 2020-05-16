package dev.krk.chipsekiz.hal;

import dev.krk.chipsekiz.interpreter.IHal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Random;


public class FramebufferHal implements IHal {
    private final Random random;
    private final Framebuffer fb;
    private final ICharacterAddressLocator characterAddressLocator;
    private boolean sound;
    private Byte key;
    private boolean dirty;

    public FramebufferHal(int width, int height, ICharacterAddressLocator characterAddressLocator) {
        this.random = new Random();
        this.characterAddressLocator = characterAddressLocator;
        this.fb = new Framebuffer(width, height);
        this.key = null;
    }

    @Override public byte getRand() {
        return (byte) random.nextInt(0x100);
    }

    @Override public void clearScreen() {
        fb.clearScreen();
    }

    @Override public boolean draw(byte x, byte y, boolean value) {
        dirty = true;
        return fb.draw(x, y, value);
    }

    @Override public void sound(boolean active) {
        sound = active;
    }

    @Override public Optional<Byte> getKey() {
        return Optional.ofNullable(key);
    }

    @Override public short getCharacterAddress(byte character) {
        return characterAddressLocator.getCharacterAddress(character);
    }

    public boolean isSoundActive() {
        return sound;
    }

    public void keyDown(byte key) {
        this.key = key;
    }

    public void keyUp() {
        this.key = null;
    }

    public String renderFramebuffer() {
        dirty = false;
        return fb.toString();
    }

    public void renderFramebuffer(int pixelWidth, int pixelHeight, OutputStream output)
        throws IOException {
        dirty = false;
        fb.writeImage(pixelWidth, pixelHeight, output);
    }

    public boolean framebufferDirty() {
        return dirty;
    }
}
