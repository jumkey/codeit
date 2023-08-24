package org.cafeboy.idea.plugin.codeit.ui;

import com.google.common.base.Stopwatch;
import com.intellij.ui.JBColor;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
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

    private final int windowX;
    private final int windowY;
    private final int windowW;
    private final int windowH;

    private final double scaleX;
    private final double scaleY;
    final Color red = JBColor.RED;
    final Color black = JBColor.BLACK;
    Color pen;
    Color brush1;
    Color brush2;

    public QRCodeSplashForm() {
        this.setName("QRCodeSplashForm");

        this.setBackground(new JBColor(()->new Color(0,0,0,0)));
        this.setVisible(true);

        windowX = User32.INSTANCE.GetSystemMetrics(WinUser.SM_XVIRTUALSCREEN);
        windowY = User32.INSTANCE.GetSystemMetrics(WinUser.SM_YVIRTUALSCREEN);
        windowW = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CXVIRTUALSCREEN);
        windowH = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CYVIRTUALSCREEN);

        AffineTransform defaultTransform = getGraphicsConfiguration().getDefaultTransform();
        scaleX = defaultTransform.getScaleX();
        scaleY = defaultTransform.getScaleY();
        setLocation(new Point((int)(windowX/scaleX),(int)(windowY/scaleY)));
        setSize(new Dimension(windowW, windowH));

        setLayout(new BorderLayout());
        setAlwaysOnTop(true);

        flashStep = 0;
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        sw = Stopwatch.createUnstarted();
        timer = new Timer((int) (ANIMATION_TIME * 1000 / ANIMATION_STEPS), this);
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
            brush1 = fromArgb((int) (30 * percent), red);
            brush2 = fromArgb((int) (100 * percent), black);
        } else {
            switch (flashStep) {
                case 0:
                    timer.setDelay(100);
                    this.setVisible(false);
                    break;
                case 1:
                    timer.setDelay(50);
                    this.setVisible(true);
                    repaint();
                    break;
                case 2:
                case 4:
                    this.setVisible(false);
                    break;
                case 3:
                case 5:
                    this.setVisible(true);
                    repaint();
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
        super.paint(g);
        final int thickness = 4;
        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy of the Graphics context
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.scale(1/scaleX,1/scaleY);

        g2d.setColor(pen);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRect(x, y, w, h);

        g2d.setColor(brush2);
        g2d.fillRect(x+ thickness/2,y+ (thickness/2),w-thickness,h -thickness);
        g2d.setColor(brush1);
        g2d.fillRect(x+ thickness/2,y+ (thickness/2),w-thickness,h -thickness);
        g2d.dispose();

    }

    public static QRCodeSplashForm show(int x, int y, int w, int h) {
        QRCodeSplashForm splash = new QRCodeSplashForm();
        // TODO: test on high DPI
        splash.targetRect = new Rectangle(x, y, w, h);

        splash.start();
        return splash;
    }
}
