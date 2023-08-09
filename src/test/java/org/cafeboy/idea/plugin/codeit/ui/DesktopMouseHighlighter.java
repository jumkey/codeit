package org.cafeboy.idea.plugin.codeit.ui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.concurrent.atomic.AtomicBoolean;

public class DesktopMouseHighlighter {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransparentHighlighter highlighter = new TransparentHighlighter();
            highlighter.start();
        });
    }

    static class TransparentHighlighter extends Thread {

        private int highlightRadius = 50;

        private AtomicBoolean running = new AtomicBoolean(true);

        @Override
        public void run() {
            TransparentWindow window = new TransparentWindow(highlightRadius);
            GlobalMouseListener mouseListener = new GlobalMouseListener(window);
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
                return;
            }
            GlobalScreen.addNativeMouseListener(mouseListener);
            GlobalScreen.addNativeMouseMotionListener(mouseListener);

            window.setVisible(true);

            while (running.get()) {
                window.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            GlobalScreen.removeNativeMouseListener(mouseListener);
            GlobalScreen.removeNativeMouseMotionListener(mouseListener);
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }

            window.dispose();
        }
    }

    static class TransparentWindow extends JFrame {

        private int highlightRadius;
        private Point mouseLocation = new Point();
        private String mouseCoordinates = "";

        public TransparentWindow(int radius) {
            this.highlightRadius = radius;

            setUndecorated(true);
            setBackground(new Color(0, 0, 0, 0));
            setSize(highlightRadius * 2, highlightRadius * 2);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            setAlwaysOnTop(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);  // Set to full-screen mode

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        System.exit(0);
                    }
                }
            });
        }

        public void setMouseLocation(Point location) {
            this.mouseLocation = location;
        }

        public void setMouseCoordinates(String coordinates) {
            this.mouseCoordinates = coordinates;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g.create(); // Create a copy of the Graphics context
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.red);
            g2d.setStroke(new BasicStroke(2));

            double scaleX = getScaleX();
            double scaleY = getScaleY();

            int x = (int) ((mouseLocation.x / scaleX) - highlightRadius);
            int y = (int) ((mouseLocation.y / scaleY) - highlightRadius);

            int ovalWidth = (int) (highlightRadius * 2);
            int ovalHeight = (int) (highlightRadius * 2);

            AffineTransform transform = new AffineTransform();
            transform.scale(scaleX, scaleY);
            g2d.setTransform(transform);

            g2d.drawOval(x, y, ovalWidth, ovalHeight);

            g2d.setColor(new Color(128, 0, 128));  // Purple color
            g2d.setFont(new Font("Arial", Font.PLAIN, (int) (14 * scaleX)));

            int textX = x + 2;
            int textY = y - 10;

            g2d.drawString(mouseCoordinates, textX, textY);

            g2d.dispose(); // Dispose of the Graphics context
        }

        private double getScaleX() {
            return getContentPane().getWidth() / (double) getWidth();
        }

        private double getScaleY() {
            return getContentPane().getHeight() / (double) getHeight();
        }
    }

    static class GlobalMouseListener implements NativeMouseInputListener {

        private TransparentWindow window;

        public GlobalMouseListener(TransparentWindow window) {
            this.window = window;
        }

        @Override
        public void nativeMouseClicked(NativeMouseEvent e) {
        }

        @Override
        public void nativeMousePressed(NativeMouseEvent e) {
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent e) {
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent e) {
            SwingUtilities.invokeLater(() -> {
                window.setMouseLocation(e.getPoint());
                window.setMouseCoordinates("X: " + e.getX() + ", Y: " + e.getY());
                window.repaint();
            });
        }

        @Override
        public void nativeMouseDragged(NativeMouseEvent e) {
        }
    }
}
