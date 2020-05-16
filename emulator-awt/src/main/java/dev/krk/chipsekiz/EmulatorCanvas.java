package dev.krk.chipsekiz;

import dev.krk.chipsekiz.hal.Framebuffer;
import dev.krk.chipsekiz.interpreter.IScreenHal;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

import static com.google.common.base.Preconditions.checkArgument;

public class EmulatorCanvas extends JPanel implements IScreenHal {
    private final Framebuffer framebuffer;

    private int scaleX;
    private int scaleY;
    private final Color emptyColor;
    private final Color occupiedColor;


    EmulatorCanvas(int emulatorWidth, int emulatorHeight, int scaleX, int scaleY, Color emptyColor,
        Color occupiedColor) {
        super();
        checkArgument(emulatorWidth > 0, "emulatorWidth must be greater than zero.");
        checkArgument(emulatorHeight > 0, "emulatorHeight must be greater than zero.");
        checkArgument(scaleX > 0, "scaleX must be greater than zero.");
        checkArgument(scaleY > 0, "scaleY must be greater than zero.");
        checkArgument(!emptyColor.equals(occupiedColor),
            "emptyColor color cannot be equal to occupiedColor color.");

        this.framebuffer = new Framebuffer(emulatorWidth, emulatorHeight);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.emptyColor = emptyColor;
        this.occupiedColor = occupiedColor;

        setBackground(emptyColor);
        setSize(emulatorWidth * scaleX, emulatorHeight * scaleY);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        framebuffer.paint(g, scaleX, scaleY, emptyColor, occupiedColor);
    }

    public void clearScreen() {
        framebuffer.clearScreen();
        repaint();
    }

    public boolean draw(byte x, byte y, boolean value) {
        boolean flipped = framebuffer.draw(x, y, value);
        repaint();
        return flipped;
    }
}
