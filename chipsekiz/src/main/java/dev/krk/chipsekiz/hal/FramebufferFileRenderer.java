package dev.krk.chipsekiz.hal;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

public class FramebufferFileRenderer {
    public void writeImage(Framebuffer framebuffer, int pixelWidth, int pixelHeight,
        OutputStream output) throws IOException {
        if (pixelWidth <= 0) throw new IllegalArgumentException("pixel width cannot be zero or negative.");
        if (pixelHeight <= 0) throw new IllegalArgumentException("pixel height cannot be zero or negative.");

        BufferedImage image = new BufferedImage(framebuffer.getWidth() * pixelWidth,
            framebuffer.getHeight() * pixelHeight, BufferedImage.TYPE_BYTE_BINARY);
        Graphics g = image.getGraphics();
        for (int y = 0; y < framebuffer.getHeight(); y++) {
            for (int x = 0; x < framebuffer.getWidth(); x++) {
                g.setColor(framebuffer.getPixel((byte) x, (byte) y) ? Color.BLACK : Color.WHITE);
                g.fillRect(x * pixelWidth, y * pixelHeight, pixelWidth, pixelHeight);
            }
        }
        ImageIO.write(image, "png", output);
    }
}
