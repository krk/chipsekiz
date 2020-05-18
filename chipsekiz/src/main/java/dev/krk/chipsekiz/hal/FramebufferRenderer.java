package dev.krk.chipsekiz.hal;

import dev.krk.chipsekiz.interpreter.IScreenHal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class FramebufferRenderer extends Framebuffer implements IScreenHal {
    private final int pixelWidth;
    private final int pixelHeight;
    private final Color emptyColor;
    private final Color occupiedColor;
    private final BufferedImage buffer;
    private final Graphics gBuffer;

    public FramebufferRenderer(int width, int height, int pixelWidth, int pixelHeight,
        Color emptyColor, Color occupiedColor) {
        super(width, height);
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        this.emptyColor = emptyColor;
        this.occupiedColor = occupiedColor;

        GraphicsConfiguration gfxConfig = GraphicsEnvironment.
            getLocalGraphicsEnvironment().getDefaultScreenDevice().
            getDefaultConfiguration();

        this.buffer = gfxConfig.createCompatibleImage(width * pixelWidth, height * pixelHeight);
        this.gBuffer = this.buffer.getGraphics();
    }

    @Override public void clearScreen() {
        super.clearScreen();

        gBuffer.setColor(emptyColor);
        gBuffer.fillRect(0, 0, getWidth() * pixelWidth, getHeight() * pixelHeight);
    }

    @Override public boolean draw(byte x, byte y, boolean value) {
        boolean flipped = super.draw(x, y, value);

        x = (byte) (Byte.toUnsignedInt(x) % getWidth());
        y = (byte) (Byte.toUnsignedInt(y) % getHeight());

        gBuffer.setColor(getPixel(x, y) ? occupiedColor : emptyColor);
        gBuffer.fillRect(x * pixelWidth, y * pixelHeight, pixelWidth, pixelHeight);

        return flipped;
    }

    public void render(Graphics g) {
        g.drawImage(buffer, 0, 0, null);
    }
}
