package org.cafeboy.idea.plugin.codeit.core;

import com.google.common.collect.ImmutableMap;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import org.cafeboy.idea.plugin.codeit.ui.QRCodeSplashForm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.List;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author jumkey
 */
public class QRCodeUtils {

    @SuppressWarnings("UndesirableClassUsage")
    public static List<String> captureScreenAndRead() {
        try {
            // 获取屏幕尺寸
            // 创建需要截取的矩形区域
            Rectangle screenRect = new Rectangle(0, 0, 0, 0);
            final Map<Rectangle, BufferedImage> map = new HashMap<>();
            for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                final Rectangle bounds = gd.getDefaultConfiguration().getBounds();
                screenRect = screenRect.union(bounds);
                // 截屏操作
                BufferedImage bufImage = new Robot(gd).createScreenCapture(bounds);
                map.put(bounds, bufImage);
            }
            // 全尺寸图
            final BufferedImage bufImage = new BufferedImage(screenRect.width, screenRect.height, BufferedImage.TYPE_INT_ARGB);
            // Draw the original image
            Graphics2D g = bufImage.createGraphics();
            int originOffsetX = screenRect.x;// 原点偏移x
            int originOffsetY = screenRect.y;// 原点偏移y
            map.forEach((bounds, image) -> g.drawImage(image, null, bounds.x - originOffsetX, bounds.y - originOffsetY));
            g.dispose();

            return readQRCode(bufImage, originOffsetX, originOffsetY);
        } catch (NotFoundException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static final Map<DecodeHintType, Object> DECODE_MAP = ImmutableMap.of(DecodeHintType.TRY_HARDER, Boolean.TRUE);

    /**
     * 读取二维码信息
     *
     * @return 二维码信息
     */
    public static List<String> readQRCode(BufferedImage bufImage, int originOffsetX, int originOffsetY) throws NotFoundException {
        final Result[] results = new QRCodeMultiReader().decodeMultiple(new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufImage))), DECODE_MAP);

        for (Result result : results) {
            Polygon p = new Polygon();
            for (ResultPoint resultPoint : result.getResultPoints()) {
                p.addPoint((int) resultPoint.getX(), (int) resultPoint.getY());
            }
            final Rectangle bounds = p.getBounds();
            QRCodeSplashForm.show(bounds.x + originOffsetX, bounds.y + originOffsetY, bounds.width, bounds.height);
        }
        return Arrays.stream(results).map(Result::getText).collect(Collectors.toList());
    }

    /**
     * 读取二维码信息
     *
     * @return 二维码信息
     */
    public static List<String> readQRCodeWithoutSplash(Image image) throws NotFoundException {
        BufferedImage bufImage = getBufferedImage(image);
        final Result[] results = new QRCodeMultiReader().decodeMultiple(new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufImage))), DECODE_MAP);
        return Arrays.stream(results).map(Result::getText).collect(Collectors.toList());
    }

    @SuppressWarnings({"UndesirableClassUsage", "UseJBColor"})
    public static BufferedImage getBufferedImage(Image image) {
        if (image instanceof BufferedImage) return (BufferedImage) image;
        Lock lock = new ReentrantLock();
        Condition size = lock.newCondition(), data = lock.newCondition();
        ImageObserver o = (img, infoflags, x, y, width, height) -> {
            lock.lock();
            try {
                if ((infoflags & ImageObserver.ALLBITS) != 0) {
                    size.signal();
                    data.signal();
                    return false;
                }
                if ((infoflags & (ImageObserver.WIDTH | ImageObserver.HEIGHT)) != 0)
                    size.signal();
                return true;
            } finally {
                lock.unlock();
            }
        };
        BufferedImage bi;
        lock.lock();
        try {
            int width, height;
            while ((width = image.getWidth(o)) < 0 || (height = image.getHeight(o)) < 0)
                size.awaitUninterruptibly();
            bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bi.createGraphics();
            try {
                g.setBackground(new Color(0, true));
                g.clearRect(0, 0, width, height);
                while (!g.drawImage(image, 0, 0, o)) data.awaitUninterruptibly();
            } finally {
                g.dispose();
            }
        } finally {
            lock.unlock();
        }
        return bi;
    }
}
