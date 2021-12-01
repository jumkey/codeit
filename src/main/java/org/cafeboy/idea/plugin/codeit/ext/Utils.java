package org.cafeboy.idea.plugin.codeit.ext;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.util.ui.ImageUtil;
import org.apache.http.util.TextUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

/**
 * @author jumkey
 */
public class Utils {

    public static void setRightMenu(JComponent jComponent, JPopupMenu menu) {
        jComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouse(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouse(e);
            }

            private void handleMouse(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public static boolean saveIcon(Icon icon, File file) {
        try {
            BufferedImage bufferedImage = getBufferedImage((ImageIcon) icon);
            ImageIO.write(bufferedImage, "png", file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void warning(String title, String msg) {
        if (TextUtils.isEmpty(title)) {
            title = Constant.APP_ID;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Notifications.Bus.notify(new Notification("Custom Notification Group", title, msg, NotificationType.WARNING));
    }

    public static void message(String title, String msg) {
        if (TextUtils.isEmpty(title)) {
            title = Constant.APP_ID;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Notifications.Bus.notify(new Notification("Custom Notification Group", title, msg, NotificationType.INFORMATION));
    }

    public static void message(String title, String msg, NotificationAction anAction) {
        if (TextUtils.isEmpty(title)) {
            title = Constant.APP_ID;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Notifications.Bus.notify(new Notification("Custom Notification Group", title, msg, NotificationType.INFORMATION).addAction(anAction));
    }

    public static BufferedImage getBufferedImage(ImageIcon icon) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        ImageObserver observer = icon.getImageObserver();
        BufferedImage bufferedImage = ImageUtil.createImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gc = bufferedImage.createGraphics();
        gc.drawImage(icon.getImage(), 0, 0, observer);
        return bufferedImage;
    }


    public static String getSuggestFileName() {
        return "Codeit_QR_CODE_" + System.currentTimeMillis();
    }

}
