package org.cafeboy.idea.plugin.codeit.ui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import javax.swing.*;
import java.awt.*;

public class ScreenPosition implements NativeMouseListener, NativeKeyListener {
    int keyCode = 0;
    TextField field = new TextField();

    public ScreenPosition() {
        JFrame jFrame = new JFrame("A");
        jFrame.setBounds(0, 0, 260, 100);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("坐标:");
        label.setPreferredSize(new Dimension(100, 20));
        panel.add(label);
        field.setPreferredSize(new Dimension(100, 20));
        panel.add(field);

        jFrame.add(panel);
        jFrame.setAlwaysOnTop(true);//设置面板总是为于其他面板之上
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        int button = e.getButton();
        if (keyCode == 56 && button == 2) {//按下Alt和右键时，显示坐标
            field.setText(e.getX() + "," + e.getY());
        }
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {

    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println(e.getKeyCode());
        keyCode = e.getKeyCode();
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        // Construct the example object.
        ScreenPosition position = new ScreenPosition();
        // Add the appropriate listeners.
        GlobalScreen.addNativeMouseListener(position);
        GlobalScreen.addNativeKeyListener(position);
    }

}

