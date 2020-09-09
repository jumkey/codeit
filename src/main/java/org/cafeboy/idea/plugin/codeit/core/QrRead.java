package org.cafeboy.idea.plugin.codeit.core;

import com.google.common.collect.ImmutableMap;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import org.cafeboy.idea.plugin.codeit.ui.QRCodeSplashForm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jumkey
 */
public class QrRead {

    public static List<String> captureScreenAndRead() {
        // 获取屏幕尺寸
        // 创建需要截取的矩形区域
        Rectangle screenRect = new Rectangle(0, 0, 0, 0);
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            screenRect = screenRect.union(gd.getDefaultConfiguration().getBounds());
        }
        try {
            // 截屏操作
            BufferedImage bufImage = new Robot().createScreenCapture(screenRect);

            return readQrCode(bufImage);
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
    public static List<String> readQrCode(BufferedImage bufImage) throws NotFoundException {
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
    public static List<String> readQrCodeWithoutSplash(BufferedImage bufImage) throws NotFoundException {
        final Result[] results = new QRCodeMultiReader().decodeMultiple(new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufImage))), DECODE_MAP);
        return Arrays.stream(results).map(Result::getText).collect(Collectors.toList());
    }
}
