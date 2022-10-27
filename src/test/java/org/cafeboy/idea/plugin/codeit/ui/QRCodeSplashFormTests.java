package org.cafeboy.idea.plugin.codeit.ui;

import org.junit.jupiter.api.Test;

/**
 * @author jumkey
 * @version 1.0, 2022/10/27
 */
public class QRCodeSplashFormTests {
    @Test
    public void testShow() {
        int x = 300;
        int y = 300;
        int w = 300;
        int h = 300;
        QRCodeSplashForm splashForm = QRCodeSplashForm.show(x, y, w, h);
        while (splashForm.isRunning()) {
            // NOOP
        }
    }
}
