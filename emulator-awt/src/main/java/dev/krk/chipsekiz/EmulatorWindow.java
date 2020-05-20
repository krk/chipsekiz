package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IScreenHal;

import javax.swing.JFrame;

import java.awt.Color;

public class EmulatorWindow extends JFrame {

    private final EmulatorCanvas canvas;

    EmulatorWindow() {
        super("chipsekiz emulator");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(64 * 12, 32 * 12 + 68);
        setResizable(false);
        setVisible(true);

        canvas = new EmulatorCanvas(64, 32, 12, 12, Color.WHITE, Color.BLACK);
        add(canvas);
    }

    public IScreenHal getScreenHal() {
        return canvas;
    }
}
