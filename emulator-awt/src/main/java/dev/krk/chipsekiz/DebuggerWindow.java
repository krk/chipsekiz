package dev.krk.chipsekiz;

import dev.krk.chipsekiz.vm.IVirtualMachine;

import javax.swing.JFrame;

import java.awt.Color;

public class DebuggerWindow extends JFrame {
    private final VMMemoryPanel memory;

    public DebuggerWindow(IVirtualMachine vm) {
        super("debugger: memory");

        // Default memory size = 4096 bytes = 32768 bits = 128x256 bits
        setSize(64 * 2, 256 * 2 * 2 + 68);
        setResizable(false);
        setVisible(true);

        memory = new VMMemoryPanel(vm, 64, 256 * 2, 2, 2, Color.WHITE, Color.BLACK);
        add(memory);
    }

    public void requestRepaint() {
        memory.requestRepaint();
    }
}
