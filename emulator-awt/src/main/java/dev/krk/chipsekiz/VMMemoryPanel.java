package dev.krk.chipsekiz;

import dev.krk.chipsekiz.vm.IVirtualMachine;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import static com.google.common.base.Preconditions.checkArgument;

public class VMMemoryPanel extends JPanel implements MouseMotionListener {
    private final BufferedImage buffer;
    private final IVirtualMachine vm;
    private final int bitWidth;
    private final int bitHeight;
    private int scaleX;
    private int scaleY;
    private final Color emptyColor;
    private final Color occupiedColor;


    VMMemoryPanel(IVirtualMachine vm, int bitWidth, int bitHeight, int scaleX, int scaleY,
        Color emptyColor, Color occupiedColor) {
        super();
        checkArgument(bitWidth > 0, "bitWidth must be greater than zero.");
        checkArgument(bitHeight > 0, "bitHeight must be greater than zero.");
        checkArgument(scaleX > 0, "scaleX must be greater than zero.");
        checkArgument(scaleY > 0, "scaleY must be greater than zero.");
        checkArgument(!emptyColor.equals(occupiedColor),
            "emptyColor color cannot be equal to occupiedColor color.");

        this.vm = vm;
        this.bitWidth = bitWidth;
        this.bitHeight = bitHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.emptyColor = emptyColor;
        this.occupiedColor = occupiedColor;
        this.buffer =
            new BufferedImage(bitWidth * scaleX, bitHeight * scaleY, BufferedImage.TYPE_INT_RGB);

        setBackground(emptyColor);
        setSize(bitWidth * scaleX, bitHeight * scaleY);

        addMouseMotionListener(this);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(buffer, 0, 0, null);
    }

    public void requestRepaint() {
        Graphics g = buffer.createGraphics();
        for (int i = 0; i < vm.getMemorySize(); i++) {
            byte b = vm.getByte(i);
            for (int j = 0; j < 8; j++) {
                int x = (i * 8 + j) % bitWidth;
                int y = 8 * i / bitWidth;

                boolean bit = (b & (1 << (7 - j))) == (1 << (7 - j));
                boolean old = buffer
                    .getRGB(x * scaleX + (scaleX > 1 ? 1 : 0), y * scaleY + (scaleY > 1 ? 1 : 0))
                    == occupiedColor.getRGB();

                g.setColor(bit ? occupiedColor : emptyColor);
                g.fillRect((x) * scaleX, y * scaleY, scaleX, scaleY);
                if (bit != old) {
                    g.setColor(Color.MAGENTA);
                    g.drawRect((x) * scaleX, y * scaleY, scaleX, scaleY);
                }
            }
        }

        // PC
        int address = vm.getPC();
        int x = (address * 8) % bitWidth;
        int y = 8 * address / bitWidth;
        g.setColor(Color.BLUE);
        g.drawRect(x * scaleX, y * scaleY, scaleX * 16, scaleY);

        // I
        address = vm.getI();
        x = (address * 8) % bitWidth;
        y = 8 * address / bitWidth;
        g.setColor(Color.ORANGE);
        g.drawRect(x * scaleX, y * scaleY, scaleX * 16, scaleY);

        super.repaint();
    }

    @Override public void mouseDragged(MouseEvent e) {
        // NOOP.
    }

    @Override public void mouseMoved(MouseEvent e) {
        int bitAddress = (e.getX() / scaleX % bitWidth) + e.getY() / scaleY * bitWidth;

        setToolTipText(String.format("address: %04X", bitAddress / 8));
    }
}
