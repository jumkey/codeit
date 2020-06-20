package org.cafeboy.idea.plugin.codeit.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.ImageUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

/**
 * 生成二维码
 *
 * @author jumkey
 */
public class ZxingApiImpl implements Api {

    @Override
    public Icon getCode(String text, String mode) {
        if (StringUtil.isEmptyOrSpaces(text)) {
            return null;
        }

        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 2);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);
            BufferedImage bufferedImage = toBufferedImage(bitMatrix);
            return new ImageIcon(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = ImageUtil.createImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        int onColor;
        int offColor;
        onColor = BLACK;
        offColor = WHITE;
        int[] pixels = new int[width * height];
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[index++] = matrix.get(x, y) ? onColor : offColor;
            }
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }
}
