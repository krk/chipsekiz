package dev.krk.chipsekiz;

import com.google.common.io.Files;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class EmulatorWindow extends JFrame implements KeyListener {
    private final IEmulatorController controller;

    EmulatorWindow(EmulatorCanvas canvas, IEmulatorController controller) {
        super("chipsekiz emulator");
        this.controller = controller;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(64 * 12, 32 * 12 + 88);

        add(canvas);

        addKeyListener(this);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("ROM");
        menu.setMnemonic('r');

        // ROM menu
        JMenuItem item = new JMenuItem("Reset");
        item.setMnemonic(KeyEvent.VK_R);
        menu.add(item);
        item.addActionListener(e -> controller.reset());

        item = new JMenuItem("Load");
        item.setMnemonic(KeyEvent.VK_L);
        menu.add(item);
        item.addActionListener(e -> {
            try {
                loadROM();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        item = new JMenuItem("Pause");
        item.setMnemonic(KeyEvent.VK_P);
        item.setActionCommand("pause");
        menu.add(item);
        JMenuItem pauseItem = item;
        item.addActionListener(e -> {
            if (e.getActionCommand().equals("pause")) {
                controller.pause();
                pauseItem.setActionCommand("resume");
                pauseItem.setText("Resume");
                pauseItem.setMnemonic(KeyEvent.VK_M);
            } else {
                controller.resume();
                pauseItem.setActionCommand("pause");
                pauseItem.setText("Pause");
                pauseItem.setMnemonic(KeyEvent.VK_P);
            }
        });

        menuBar.add(menu);

        // Sound menu
        menu = new JMenu("Sound");
        menu.setMnemonic('u');

        ActionListener soundSetter =
            e -> controller.setToneFrequency(Integer.parseInt(e.getActionCommand()));

        ButtonGroup soundGroup = new ButtonGroup();

        JRadioButtonMenuItem sound = new JRadioButtonMenuItem("Off", false);
        sound.setMnemonic(KeyEvent.VK_0);
        sound.setActionCommand("0");
        menu.add(sound);
        sound.addActionListener(soundSetter);
        soundGroup.add(sound);

        sound = new JRadioButtonMenuItem("440 Hz", false);
        sound.setActionCommand("440");
        menu.add(sound);
        sound.addActionListener(soundSetter);
        soundGroup.add(sound);

        sound = new JRadioButtonMenuItem("1600 Hz", true);
        sound.setActionCommand("1600");
        menu.add(sound);
        sound.addActionListener(soundSetter);
        soundGroup.add(sound);

        sound = new JRadioButtonMenuItem("2600 Hz", false);
        sound.setActionCommand("2600");
        menu.add(sound);
        sound.addActionListener(soundSetter);
        soundGroup.add(sound);

        sound = new JRadioButtonMenuItem("5000 Hz", false);
        sound.setActionCommand("5000");
        menu.add(sound);
        sound.addActionListener(soundSetter);
        soundGroup.add(sound);

        menuBar.add(menu);

        // Speed menu
        menu = new JMenu("Speed");
        menu.setMnemonic('e');
        ActionListener speedSetter =
            e -> controller.setFrequency(Integer.parseInt(e.getActionCommand()));

        ButtonGroup speedGroup = new ButtonGroup();

        JRadioButtonMenuItem speed = new JRadioButtonMenuItem("10 Hz", false);
        speed.setActionCommand("10");
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        speed = new JRadioButtonMenuItem("50 Hz", false);
        speed.setActionCommand("50");
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        speed = new JRadioButtonMenuItem("100 Hz", false);
        speed.setActionCommand("100");
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        speed = new JRadioButtonMenuItem("200 Hz", false);
        speed.setActionCommand("200");
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        speed = new JRadioButtonMenuItem("500 Hz", false);
        speed.setActionCommand("500");
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        speed = new JRadioButtonMenuItem("1000 Hz", false);
        speed.setActionCommand("1000");
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        speed = new JRadioButtonMenuItem("5000 Hz", true);
        speed.setActionCommand("5000");
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        speed = new JRadioButtonMenuItem("Unlock item", false);
        speed.setActionCommand(Integer.toString(Integer.MAX_VALUE));
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private void loadROM() throws IOException {
        final JFileChooser fc = new JFileChooser();

        fc.setDialogTitle("Select CHIP-8 ROM");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            byte[] program = Files.toByteArray(fc.getSelectedFile());
            controller.load(0x200, program);
        }
    }

    @Override public void keyTyped(KeyEvent e) {
        // NOOP.
    }

    @Override public void keyPressed(KeyEvent e) {
        controller.keyDown(e.getKeyChar());
    }

    @Override public void keyReleased(KeyEvent e) {
        controller.keyUp();
    }
}
