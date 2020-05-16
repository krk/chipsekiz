package dev.krk.chipsekiz.hal;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkArgument;

public class Framebuffer {
    private final boolean[][] buffer;
    private final int width;
    private final int height;

    public Framebuffer(int width, int height) {
        checkArgument(width > 0 && width <= 0xFF, "width out of bounds.");
        checkArgument(height > 0 && height <= 0xFF, "height out of bound.");

        this.width = width;
        this.height = height;

        buffer = new boolean[height][width];
    }

    public void clearScreen() {
        for (int i = 0; i < buffer.length; i++) {
            for (int j = 0; j < width; j++) {
                buffer[i][j] = false;
            }
        }
    }

    public boolean draw(byte x, byte y, boolean value) {
        x = (byte) (Byte.toUnsignedInt(x) % width);
        y = (byte) (Byte.toUnsignedInt(y) % height);

        boolean old = buffer[y][x];
        buffer[y][x] = old ^ value;

        // Return true if a bit was turned off.
        return old && value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getPixel(byte x, byte y) {
        return buffer[y][x];
    }

    public void writeImage(int pixelWidth, int pixelHeight, OutputStream output)
        throws IOException {
        checkArgument(pixelWidth > 0, "pixel width cannot be zero or negative.");
        checkArgument(pixelHeight > 0, "pixel height cannot be zero or negative.");

        int width = pixelWidth * getWidth();
        int height = pixelHeight * getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = image.createGraphics();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                byte color = (byte) (getPixel((byte) (x / pixelWidth), (byte) (y / pixelHeight)) ?
                    0x00 :
                    0xFF);
                if (color == 0x00) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.drawLine(x, y, x, y);
            }
        }

        ImageIO.write(image, "png", output);
    }

    @Override public String toString() {
        StringBuilder builder = new StringBuilder((height + 1) * width);

        for (int i = -1; i <= height; i++) {
            if (i == -1) {
                builder.append("┌");
            } else if (i == height) {
                builder.append("└");
            }

            for (int j = -1; j <= width; j++) {
                if (i == -1 || i == height) {
                    if (j >= 0 && j < width) {
                        builder.append("─");
                    }
                    continue;
                }
                if (j == -1 || j == width) {
                    builder.append("│");
                    continue;
                }

                builder.append(buffer[i][j] ? "█" : " ");
            }

            if (i == -1) {
                builder.append("┐");
            } else if (i == height) {
                builder.append("┘");
            }

            builder.append("\n");
        }
        return builder.toString();
    }
}
