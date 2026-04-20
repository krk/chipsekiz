package dev.krk.chipsekiz.hal;

public class Framebuffer {
    private final boolean[][] buffer;
    private final int width;
    private final int height;

    public Framebuffer(int width, int height) {
        if (width <= 0 || width > 0xFF) throw new IllegalArgumentException("width out of bounds.");
        if (height <= 0 || height > 0xFF) throw new IllegalArgumentException("height out of bound.");

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

    public void scroll(Direction direction, int pixels) {
        if (pixels < 0) throw new IllegalArgumentException("pixels out of bounds.");

        if (direction == Direction.Left || direction == Direction.Up) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    switch (direction) {
                        case Left -> buffer[i][j] =
                            j >= width - pixels ? false : buffer[i][j + pixels];
                        case Up -> buffer[i][j] =
                            i >= height - pixels ? false : buffer[i + pixels][j];
                    }
                }
            }
        } else {
            for (int i = height - 1; i >= 0; i--) {
                for (int j = width - 1; j >= 0; j--) {
                    switch (direction) {
                        case Right -> buffer[i][j] = j >= pixels ? buffer[i][j - pixels] : false;
                        case Down -> buffer[i][j] = i >= pixels ? buffer[i - pixels][j] : false;
                    }
                }
            }
        }
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
