package org.cafeboy.idea.plugin.codeit.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
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
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 2);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);
            int fontColor, backColor, offColor;
            switch (mode) {
                case MODE_0:
                    fontColor = backColor = BLACK;
                    offColor = WHITE;
                    break;
                case MODE_1:
                    fontColor = 0xFFF2668B;
                    backColor = 0xFF1876F4;
                    offColor = WHITE;
                    break;
                case MODE_2:
                    fontColor = 0xFF1876F4;
                    backColor = 0xFFF2668B;
                    offColor = WHITE;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + mode);
            }
            BufferedImage bufferedImage = toBufferedImage(bitMatrix, fontColor, backColor, offColor);
            return new ImageIcon(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;

    @SuppressWarnings("UndesirableClassUsage")
    public static BufferedImage toBufferedImage(BitMatrix matrix, int fromColor, int toColor, int offColor) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int onColor = getColorInt(fromColor, toColor, height, y);
                pixels[y * width + x] = matrix.get(x, y) ? onColor : offColor;
            }
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    @SuppressWarnings("UseJBColor")
    private static int getColorInt(int fromColor, int toColor, int height, int y) {
        // 二维码颜色（RGB）
        Color from = new Color(fromColor);
        Color to = new Color(toColor);
        int num1 = (int) (from.getRed() - (from.getRed() - to.getRed()) * y / (height - 1.0));
        int num2 = (int) (from.getGreen() - (from.getGreen() - to.getGreen()) * y / (height - 1.0));
        int num3 = (int) (from.getBlue() - (from.getBlue() - to.getBlue()) * y / (height - 1.0));
        Color color = new Color(num1, num2, num3);
        return color.getRGB();
    }
}
