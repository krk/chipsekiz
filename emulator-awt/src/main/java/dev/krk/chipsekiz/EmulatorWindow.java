package dev.krk.chipsekiz;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class EmulatorWindow extends JFrame {

    EmulatorWindow(EmulatorCanvas canvas, IEmulatorController controller) {
        super("chipsekiz emulator");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(64 * 12, 32 * 12 + 88);

        add(canvas);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Sound");
        menu.setMnemonic('u');

        // Sound menu
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

        speed = new JRadioButtonMenuItem("Unlock sound", false);
        speed.setActionCommand(Integer.toString(Integer.MAX_VALUE));
        menu.add(speed);
        speed.addActionListener(speedSetter);
        speedGroup.add(speed);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        setVisible(true);
    }
}
