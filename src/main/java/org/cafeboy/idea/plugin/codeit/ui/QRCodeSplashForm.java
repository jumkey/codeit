package org.cafeboy.idea.plugin.codeit.ui;

import com.google.common.base.Stopwatch;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * @author jumkey
 */
public class QRCodeSplashForm extends JWindow implements ActionListener {
    public Rectangle targetRect;

    private final Timer timer;
    private int flashStep;
    /**
     * resolution is 15ms
     */
    private static final double FPS = 1.0 / 15 * 1000;
    private static final double ANIMATION_TIME = 0.5;
    private static final int ANIMATION_STEPS = (int) (ANIMATION_TIME * FPS);
    private final Stopwatch sw;
    private int x;
    private int y;
    private int w;
    private int h;
    final Color transparent = fromArgb(0, JBColor.BLACK);
    final Color red = JBColor.RED;
    Color pen;
    Color brush;

    public QRCodeSplashForm() {
        this.setName("QRCodeSplashForm");

        this.setBackground(transparent);
        flashStep = 0;
        x = 0;
        y = 0;
        w = this.getWidth();
        h = this.getHeight();
        sw = Stopwatch.createUnstarted();
        timer = new Timer((int) (ANIMATION_TIME * 1000 / ANIMATION_STEPS), this);
        pen = red;
        brush = fromArgb(30, red);
    }

    public void start() {
        sw.start();
        timer.start();
    }

    public boolean isRunning() {
        return timer.isRunning();
    }

    @SuppressWarnings("UseJBColor")
    Color fromArgb(int a, Color c) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double percent = (double) sw.elapsed(TimeUnit.MILLISECONDS) / 1000.0 / ANIMATION_TIME;
        if (percent < 1) {
            // ease out
            percent = 1 - Math.pow((1 - percent), 4);
            x = (int) (targetRect.getX() * percent);
            y = (int) (targetRect.getY() * percent);
            w = (int) (targetRect.getWidth() * percent + 1000 * (1 - percent));
            h = (int) (targetRect.getHeight() * percent + 1000 * (1 - percent));

            pen = fromArgb((int) (255 * percent), red);
            brush = fromArgb((int) (30 * percent), red);
            this.setLocation(new Point(x, y));
            this.setSize(w, h);
            this.setVisible(true);
        } else {
            switch (flashStep) {
                case 0:
                    timer.setDelay(100);
                    this.setVisible(false);
                    break;
                case 1:
                    timer.setDelay(50);
                    this.setVisible(true);
                    break;
                case 2:
                case 4:
                    this.setVisible(false);
                    break;
                case 3:
                case 5:
                    this.setVisible(true);
                    break;
                default:
                    sw.stop();
                    timer.stop();
                    this.dispose();
                    break;
            }
            flashStep++;
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        final int thickness = 4;
        Graphics2D gg = (Graphics2D) g;
        gg.clearRect(0, 0, getSize().width, getSize().height);
        gg.setColor(brush);
        gg.fillRect(0, 0, getSize().width, getSize().height);
        gg.setColor(pen);
        gg.setStroke(new BasicStroke(thickness));
        gg.drawRect(thickness / 2, thickness / 2, w - thickness, h - thickness);
    }

    public static QRCodeSplashForm show(int x, int y, int w, int h) {
        QRCodeSplashForm splash = new QRCodeSplashForm();
        // TODO: test on high DPI
        splash.targetRect = new Rectangle(x, y, w, h);

        splash.start();
        return splash;
    }
}
