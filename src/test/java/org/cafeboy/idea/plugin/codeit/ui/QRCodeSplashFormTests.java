package org.cafeboy.idea.plugin.codeit.ui;

import org.junit.jupiter.api.Test;

/**
 * @author jumkey
 * @version 1.0, 2022/10/27
 */
public class QRCodeSplashFormTests {
    @Test
    public void testShow() {
        int x = 600;
        int y = 400;
        int w = 100;
        int h = 100;
        QRCodeSplashForm splashForm = QRCodeSplashForm.show(x, y, w, h);
        while (splashForm.isRunning()) {
            // NOOP
        }
    }
}
