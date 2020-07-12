package dev.krk.chipsekiz.emulator;

import com.google.common.io.Files;
import dev.krk.chipsekiz.superchip.interpreter.Resolution;

import javax.sound.sampled.LineUnavailableException;
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
import java.util.Timer;
import java.util.TimerTask;

public class EmulatorWindow extends JFrame implements KeyListener {
    private IEmulatable emulatable;
    private EmulatorCanvas canvas;
    private IEmulatorController controller;
    private final Tone tone;
    private String vmName = "CHIP-8";

    EmulatorWindow(IEmulatableFactory factory, EmulatorOptions options, Resolution resolution)
        throws LineUnavailableException {
        super("chipsekiz emulator");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tone = new Tone(1600);

        loadEmulator(factory, options, resolution);

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

        menu = new JMenu("VM");
        menu.setMnemonic('v');

        ButtonGroup vmGroup = new ButtonGroup();

        JRadioButtonMenuItem vm = new JRadioButtonMenuItem("CHIP-8", true);
        vm.setMnemonic(KeyEvent.VK_0);
        menu.add(vm);
        vm.addActionListener(e -> {
            loadEmulator(new Chip8EmulatableFactory(), options, Resolution.Low);
            vmName = "CHIP-8";
            runAsync();
        });
        vmGroup.add(vm);

        vm = new JRadioButtonMenuItem("Super CHIP-8", false);
        menu.add(vm);
        vm.addActionListener(e -> {
            loadEmulator(new SuperChip8EmulatableFactory(), options, Resolution.High);
            vmName = "SuperCHIP-8";
            runAsync();
        });
        vmGroup.add(vm);
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

        // Scale menu
        menu = new JMenu("Scale");
        menu.setMnemonic('c');

        ActionListener scaleSetter = e -> {
            int scale = Integer.parseInt(e.getActionCommand());
            controller.pause();
            canvas.rescale(scale, scale);
            setSize(canvas.getWidth(), canvas.getHeight() + 88);
            controller.resume();
        };

        ButtonGroup scaleGroup = new ButtonGroup();

        JRadioButtonMenuItem scale = new JRadioButtonMenuItem("1x1", false);
        scale.setMnemonic(KeyEvent.VK_1);
        scale.setActionCommand("1");
        menu.add(scale);
        scale.addActionListener(scaleSetter);
        scaleGroup.add(scale);

        scale = new JRadioButtonMenuItem("2x2", false);
        scale.setMnemonic(KeyEvent.VK_2);
        scale.setActionCommand("2");
        menu.add(scale);
        scale.addActionListener(scaleSetter);
        scaleGroup.add(scale);

        scale = new JRadioButtonMenuItem("3x3", false);
        scale.setMnemonic(KeyEvent.VK_3);
        scale.setActionCommand("3");
        menu.add(scale);
        scale.addActionListener(scaleSetter);
        scaleGroup.add(scale);

        scale = new JRadioButtonMenuItem("4x4", false);
        scale.setMnemonic(KeyEvent.VK_4);
        scale.setActionCommand("4");
        menu.add(scale);
        scale.addActionListener(scaleSetter);
        scaleGroup.add(scale);

        scale = new JRadioButtonMenuItem("8x8", false);
        scale.setMnemonic(KeyEvent.VK_8);
        scale.setActionCommand("8");
        menu.add(scale);
        scale.addActionListener(scaleSetter);
        scaleGroup.add(scale);

        scale = new JRadioButtonMenuItem("12x12", true);
        scale.setActionCommand("12");
        menu.add(scale);
        scale.addActionListener(scaleSetter);
        scaleGroup.add(scale);

        scale = new JRadioButtonMenuItem("16x16", false);
        scale.setMnemonic(KeyEvent.VK_5);
        scale.setActionCommand("16");
        menu.add(scale);
        scale.addActionListener(scaleSetter);
        scaleGroup.add(scale);

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

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                setTitle(String.format("chipsekiz %s emulator - %d Hz", vmName,
                    controller.getActualFrequency()));
            }
        }, 0, 100);
    }

    public void run() {
        if (!emulatable.hasDemoProgram()) {
            controller.pause();
        }
        controller.run();
    }

    private void runAsync() {
        new Thread(this::run).start();
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

    private void loadEmulator(IEmulatableFactory factory, EmulatorOptions options,
        Resolution resolution) {

        emulatable = factory.create(options, tone);
        EmulatorCanvas canvas = emulatable.getCanvas();
        IEmulatorController controller = emulatable.getController();

        if (this.controller != null) {
            this.controller.stop();
        }

        setSize((resolution == Resolution.High ? 128 : 64) * 12,
            (resolution == Resolution.High ? 64 : 32) * 12 + 88);

        if (this.canvas != null) {
            remove(this.canvas);
        }
        add(canvas);

        this.canvas = canvas;
        this.controller = controller;
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

    @Override public void dispose() {
        super.dispose();

        tone.close();
    }
}
