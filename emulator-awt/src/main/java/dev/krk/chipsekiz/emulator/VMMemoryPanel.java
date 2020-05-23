package dev.krk.chipsekiz.emulator;

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
    private final int scaleX;
    private final int scaleY;
    private final Color emptyColor;
    private final Color occupiedColor;
    private int address;
    private int regPC;
    private short regI;

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

        highlightBits(g, address, 8, Color.MAGENTA);
        highlightBits(g, regPC, 16, Color.BLUE);
        highlightBits(g, regI, 16, Color.ORANGE);
    }

    public void requestRepaint(VMUpdateKind updateKind, int address) {
        Graphics g = buffer.createGraphics();

        switch (updateKind) {
            case All -> {
                for (int i = 0; i < vm.getMemorySize(); i++) {
                    paintByte(g, i);
                }
                requestRepaint(VMUpdateKind.PC, 0);
                requestRepaint(VMUpdateKind.I, 0);
            }
            case Address -> {
                paintByte(g, address);
                this.address = address;
            }
            case PC -> regPC = vm.getPC();
            case I -> regI = vm.getI();
        }

        super.repaint();
    }

    private void highlightBits(Graphics g, int address, int bits, Color color) {
        int x = (address * 8) % bitWidth;
        int y = 8 * address / bitWidth;
        g.setColor(color);
        g.drawRect(x * scaleX, y * scaleY, scaleX * bits, scaleY);
    }

    private void paintByte(Graphics g, int i) {
        byte b = vm.getByte(i);
        for (int j = 0; j < 8; j++) {
            int x = (i * 8 + j) % bitWidth;
            int y = 8 * i / bitWidth;

            g.setColor((b & (1 << (7 - j))) == (1 << (7 - j)) ? occupiedColor : emptyColor);
            g.fillRect((x) * scaleX, y * scaleY, scaleX, scaleY);
        }
    }

    @Override public void mouseDragged(MouseEvent e) {
        // NOOP.
    }

    @Override public void mouseMoved(MouseEvent e) {
        int bitAddress = (e.getX() / scaleX % bitWidth) + e.getY() / scaleY * bitWidth;

        setToolTipText(String
            .format("address: %04X, value: %02X", bitAddress / 8, vm.getByte(bitAddress / 8)));
    }
}
