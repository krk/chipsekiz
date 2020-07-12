package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.hal.FramebufferRenderer;
import dev.krk.chipsekiz.interpreter.IScreenHal;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

import static com.google.common.base.Preconditions.checkArgument;


public class EmulatorCanvas extends JPanel implements IScreenHal {
    protected FramebufferRenderer renderer;
    protected int emulatorWidth;
    protected int emulatorHeight;
    private int scaleX;
    private int scaleY;
    private final Color emptyColor;
    private final Color occupiedColor;
    private int offsetX;
    private int offsetY;

    EmulatorCanvas(int emulatorWidth, int emulatorHeight, int scaleX, int scaleY, Color emptyColor,
        Color occupiedColor) {
        super();
        this.emptyColor = emptyColor;
        this.occupiedColor = occupiedColor;
        checkArgument(emulatorWidth > 0, "emulatorWidth must be greater than zero.");
        checkArgument(emulatorHeight > 0, "emulatorHeight must be greater than zero.");
        checkArgument(!emptyColor.equals(occupiedColor),
            "emptyColor color cannot be equal to occupiedColor color.");

        this.scaleX = scaleX;
        this.scaleY = scaleY;

        resizeEmulator(emulatorWidth, emulatorHeight);

        setBackground(emptyColor);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Center buffer on canvas.
        this.offsetX = Math.max(0, (getWidth() - renderer.getWidth()) / 2);
        this.offsetY = Math.max(0, (getHeight() - renderer.getHeight()) / 2);
        g.translate(offsetX, offsetY);

        renderer.render(g);
    }

    public void clearScreen() {
        renderer.clearScreen();
        repaint();
    }

    public boolean draw(byte x, byte y, boolean value) {
        boolean flipped = renderer.draw(x, y, value);
        repaint(50, x * scaleX + offsetX, y * scaleY + offsetY, scaleX, scaleY);
        return flipped;
    }

    public void rescale(int scaleX, int scaleY) {
        checkArgument(scaleX > 0, "scaleX must be greater than zero.");
        checkArgument(scaleY > 0, "scaleY must be greater than zero.");

        FramebufferRenderer renderer =
            new FramebufferRenderer(emulatorWidth, emulatorHeight, scaleX, scaleY, emptyColor,
                occupiedColor);

        if (this.renderer != null) {
            for (int y = 0; y < emulatorHeight; y++) {
                for (int x = 0; x < emulatorWidth; x++) {
                    renderer.draw((byte) x, (byte) y, this.renderer.getPixel((byte) x, (byte) y));
                }
            }
        }

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.renderer = renderer;
        setSize(emulatorWidth * scaleX, emulatorHeight * scaleY);
    }

    public void resizeEmulator(int emulatorWidth, int emulatorHeight) {
        checkArgument(emulatorWidth > 0, "emulatorWidth must be greater than zero.");
        checkArgument(emulatorHeight > 0, "emulatorHeight must be greater than zero.");

        this.emulatorWidth = emulatorWidth;
        this.emulatorHeight = emulatorHeight;

        rescale(scaleX, scaleY);
    }
}
