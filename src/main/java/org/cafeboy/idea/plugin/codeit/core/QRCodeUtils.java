package org.cafeboy.idea.plugin.codeit.core;

import com.google.common.collect.ImmutableMap;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.lang.JavaVersion;
import org.cafeboy.idea.plugin.codeit.ui.QRCodeSplashForm;
import org.jetbrains.annotations.NotNull;
import sun.awt.ComponentFactory;

import java.awt.*;
import java.awt.image.*;
import java.awt.peer.RobotPeer;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author jumkey
 */
public class QRCodeUtils {

    private static final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    public static List<String> captureScreenAndRead() {
        try {
            Rectangle screenRect = getFullVirtualScreenRect();
            // 创建多屏幕的全尺寸图片
            BufferedImage screenCapture = screenshot(screenRect);
            return readQRCode(screenCapture);
        } catch (NotFoundException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @NotNull
    public static Rectangle getFullVirtualScreenRect() {
        GraphicsDevice[] screenDevices = localGraphicsEnvironment.getScreenDevices();
        Rectangle allBounds = new Rectangle();
        for (int i = 0; i < screenDevices.length; i++) {
            GraphicsDevice screenDevice = screenDevices[i];
            GraphicsConfiguration gc = screenDevice.getDefaultConfiguration();
            // 获取 GraphicsConfiguration 的 Bounds，它包含更高分辨率信息
            Rectangle screenBounds = gc.getBounds();
            if (SystemInfo.isWindows) {
                screenBounds.width = screenDevice.getDisplayMode().getWidth();
                screenBounds.height = screenDevice.getDisplayMode().getHeight();
            }
            allBounds.add(screenBounds);
        }
        return allBounds;
    }

    public static BufferedImage screenshot(Rectangle allBounds) {
        final RobotPeer robotPeer;
        final MethodType methodType;
        try {
            if (JavaVersion.current().feature < 17) {
                methodType = MethodType.methodType(RobotPeer.class, Robot.class, GraphicsDevice.class);
                MethodHandle methodHandle = lookup.findVirtual(ComponentFactory.class, "createRobot", methodType).bindTo(toolkit);
                robotPeer = (RobotPeer) methodHandle.invokeExact((Robot) null, localGraphicsEnvironment.getDefaultScreenDevice());
            } else {
                methodType = MethodType.methodType(RobotPeer.class, GraphicsDevice.class);
                MethodHandle methodHandle = lookup.findVirtual(ComponentFactory.class, "createRobot", methodType).bindTo(toolkit);
                robotPeer = (RobotPeer) methodHandle.invokeExact(localGraphicsEnvironment.getDefaultScreenDevice());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        int[] pixels = robotPeer.getRGBPixels(allBounds);
        DirectColorModel screenCapCM = new DirectColorModel(24,
                /* red mask */ 0x00FF0000,
                /* green mask */ 0x0000FF00,
                /* blue mask */ 0x000000FF);
        DataBufferInt buffer = new DataBufferInt(pixels, pixels.length);
        int[] bandmasks = new int[3];
        bandmasks[0] = screenCapCM.getRedMask();
        bandmasks[1] = screenCapCM.getGreenMask();
        bandmasks[2] = screenCapCM.getBlueMask();

        WritableRaster raster = Raster.createPackedRaster(buffer, allBounds.width,
                allBounds.height, allBounds.width, bandmasks, null);
        BufferedImage highResolutionImage = new BufferedImage(screenCapCM, raster, false, null);

        return highResolutionImage;
    }


    /**
     * TODO: 此方法在 JDK8 和 JDK11 环境下执行结果不一致，故被弃用
     *
     * @return
     * @throws AWTException
     */
    @Deprecated
    @NotNull
    private static BufferedImage getScreenshotImg() throws AWTException {
        // 获取屏幕尺寸
        // 创建需要截取的矩形区域
        Rectangle screenRect = new Rectangle(0, 0, 0, 0);
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            final Rectangle bounds = gd.getDefaultConfiguration().getBounds();
            screenRect = screenRect.union(bounds);
        }
        return new Robot().createScreenCapture(screenRect);
    }

    private static final Map<DecodeHintType, Object> DECODE_MAP = ImmutableMap.of(DecodeHintType.TRY_HARDER, Boolean.TRUE);

    /**
     * 读取二维码信息
     *
     * @return 二维码信息
     */
    public static List<String> readQRCode(BufferedImage bufImage) throws NotFoundException {
        final Result[] results = new QRCodeMultiReader().decodeMultiple(new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufImage))), DECODE_MAP);

        for (Result result : results) {
            Polygon p = new Polygon();
            for (ResultPoint resultPoint : result.getResultPoints()) {
                p.addPoint((int) resultPoint.getX(), (int) resultPoint.getY());
            }
            final Rectangle bounds = p.getBounds();
            QRCodeSplashForm.show(bounds.x, bounds.y, bounds.width, bounds.height);
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
