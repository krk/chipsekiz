package dev.krk.chipsekiz.hal;

import dev.krk.chipsekiz.interpreter.IHal;

import java.util.Optional;
import java.util.Random;


public class FramebufferHal implements IHal {
    private final Random random;
    private final Framebuffer fb;
    private boolean sound;
    private Byte key;
    private boolean dirty;

    public FramebufferHal(int width, int height) {
        this(new Framebuffer(width, height));
    }

    public FramebufferHal(Framebuffer framebuffer) {
        this.random = new Random();
        this.fb = framebuffer;
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

    public boolean framebufferDirty() {
        return dirty;
    }
}
