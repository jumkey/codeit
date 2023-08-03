package org.cafeboy.idea.plugin.codeit.ui;

import org.apache.xmlgraphics.image.writer.ImageWriterUtil;
import org.cafeboy.idea.plugin.codeit.core.QRCodeUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenAndReadTest {
    @Test
    public void testRead() {
        while (true) {
        QRCodeUtils.captureScreenAndRead();
        }
    }
    @Test
    public void screenshotTest() throws IOException {
        BufferedImage image = QRCodeUtils.getPrimaryScreenshotImage();
        ImageWriterUtil.saveAsPNG(image,new File("D:\\test1.png"));
    }
    @Test
    public void screenshotTest2() throws IOException {
        BufferedImage image = QRCodeUtils.getFullVirtualScreenshotImage();
        ImageWriterUtil.saveAsPNG(image,new File("D:\\test2.png"));
    }


}
