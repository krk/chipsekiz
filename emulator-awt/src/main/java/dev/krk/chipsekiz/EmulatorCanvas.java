package dev.krk.chipsekiz;

import dev.krk.chipsekiz.hal.FramebufferRenderer;
import dev.krk.chipsekiz.interpreter.IScreenHal;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

import static com.google.common.base.Preconditions.checkArgument;

public class EmulatorCanvas extends JPanel implements IScreenHal {
    private FramebufferRenderer renderer;
    private final int emulatorWidth;
    private final int emulatorHeight;
    private int scaleX;
    private int scaleY;
    private final Color emptyColor;
    private final Color occupiedColor;

    EmulatorCanvas(int emulatorWidth, int emulatorHeight, int scaleX, int scaleY, Color emptyColor,
        Color occupiedColor) {
        super();
        this.emptyColor = emptyColor;
        this.occupiedColor = occupiedColor;
        checkArgument(emulatorWidth > 0, "emulatorWidth must be greater than zero.");
        checkArgument(emulatorHeight > 0, "emulatorHeight must be greater than zero.");
        checkArgument(!emptyColor.equals(occupiedColor),
            "emptyColor color cannot be equal to occupiedColor color.");

        this.emulatorWidth = emulatorWidth;
        this.emulatorHeight = emulatorHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        rescale(scaleX, scaleY);

        setBackground(emptyColor);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderer.render(g);
    }

    public void clearScreen() {
        renderer.clearScreen();
        repaint();
    }

    public boolean draw(byte x, byte y, boolean value) {
        boolean flipped = renderer.draw(x, y, value);
        repaint(50, x * scaleX, y * scaleY, scaleX, scaleY);
        return flipped;
    }

    public void rescale(int scaleX, int scaleY) {
        checkArgument(scaleX > 0, "scaleX must be greater than zero.");
        checkArgument(scaleY > 0, "scaleY must be greater than zero.");

        this.scaleX = scaleX;
        this.scaleY = scaleY;

        this.renderer =
            new FramebufferRenderer(emulatorWidth, emulatorHeight, scaleX, scaleY, emptyColor,
                occupiedColor);

        setSize(emulatorWidth * scaleX, emulatorHeight * scaleY);
    }
}
